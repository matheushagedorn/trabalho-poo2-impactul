package com.example.application.database.dataprovider;

import com.example.application.dao.GanhoDao;
import com.example.application.dto.Ganho;
import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;

public class GanhoDataProvider extends AbstractBackEndDataProvider<Ganho, CrudFilter> {
    public static final List<Ganho> DATABASE = new ArrayList<>(GanhoDao.buscarTodosGanhos());

    private Consumer<Long> sizeChangeListener;

    @Override
    protected Stream<Ganho> fetchFromBackEnd(Query<Ganho, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<Ganho> stream = DATABASE.stream();

        if (query.getFilter().isPresent()) {
            stream = stream.filter(predicate(query.getFilter().get()))
                    .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<Ganho, CrudFilter> query) {
        long count = fetchFromBackEnd(query).count();

        if (sizeChangeListener != null) {
            sizeChangeListener.accept(count);
        }

        return (int) count;
    }

    void setSizeChangeListener(Consumer<Long> listener) {
        sizeChangeListener = listener;
    }

    private static Predicate<Ganho> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
                .map(constraint -> (Predicate<Ganho>) ganho -> {
                    try {
                        Object value = valueOf(constraint.getKey(), ganho);
                        return value != null && value.toString().toLowerCase()
                                .contains(constraint.getValue().toLowerCase());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }).reduce(Predicate::and).orElse(e -> true);
    }

    private static Comparator<Ganho> comparator(CrudFilter filter) {
        // For RDBMS just generate an ORDER BY clause
        return filter.getSortOrders().entrySet().stream().map(sortClause -> {
            try {
                Comparator<Ganho> comparator = Comparator.comparing(
                        ganho -> (Comparable) valueOf(sortClause.getKey(),
                                ganho));

                if (sortClause.getValue() == SortDirection.DESCENDING) {
                    comparator = comparator.reversed();
                }

                return comparator;

            } catch (Exception ex) {
                return (Comparator<Ganho>) (o1, o2) -> 0;
            }
        }).reduce(Comparator::thenComparing).orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, Ganho ganho) {
        try {
            Field field = Ganho.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(ganho);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void persist(Ganho item) {
        if (item.getId() == null) {
            item.setId(DATABASE.stream().map(Ganho::getId).max(naturalOrder())
                    .orElse(0) + 1);
        }

        final Optional<Ganho> existingItem = find(item.getId());
        if (existingItem.isPresent()) {
            int position = DATABASE.indexOf(existingItem.get());
            DATABASE.remove(existingItem.get());
            DATABASE.add(position, item);
            Ganho ganho = GanhoDao.buscarGanhoPorId(item.getId());
            GanhoDao.atualizarGanho(ganho);
            Notification notification = Notification.show("Ganho atualizado com sucesso!",
                    5000, Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            DATABASE.add(item);
            GanhoDao.adicionarGanho(item);
            Notification notification = Notification.show("Ganho adicionado com sucesso!",
                    5000, Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }

    Optional<Ganho> find(Integer id) {
        return DATABASE.stream().filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public void delete(Ganho item) {
        DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
        GanhoDao.deletarGanho(item.getId());
        Notification notification = Notification.show("Ganho removido com sucesso!",
                5000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
    }
}
