package com.example.application.database.dataprovider;

import com.example.application.dao.GastoDao;
import com.example.application.entity.dto.Gasto;
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

public class GastoDataProvider extends AbstractBackEndDataProvider<Gasto, CrudFilter> {
        final List<Gasto> DATABASE = new ArrayList<>(GastoDao.buscarTodosGastos());

        private Consumer<Long> sizeChangeListener;

        @Override
        protected Stream<Gasto> fetchFromBackEnd(Query<Gasto, CrudFilter> query) {
            int offset = query.getOffset();
            int limit = query.getLimit();

            Stream<Gasto> stream = DATABASE.stream();

            if (query.getFilter().isPresent()) {
                stream = stream.filter(predicate(query.getFilter().get()))
                        .sorted(comparator(query.getFilter().get()));
            }

            return stream.skip(offset).limit(limit);
        }

        @Override
        protected int sizeInBackEnd(Query<Gasto, CrudFilter> query) {
            // For RDBMS just execute a SELECT COUNT(*) ... WHERE query
            long count = fetchFromBackEnd(query).count();

            if (sizeChangeListener != null) {
                sizeChangeListener.accept(count);
            }

            return (int) count;
        }

        void setSizeChangeListener(Consumer<Long> listener) {
            sizeChangeListener = listener;
        }

        private static Predicate<Gasto> predicate(CrudFilter filter) {
            // For RDBMS just generate a WHERE clause
            return filter.getConstraints().entrySet().stream()
                    .map(constraint -> (Predicate<Gasto>) gasto -> {
                        try {
                            Object value = valueOf(constraint.getKey(), gasto);
                            return value != null && value.toString().toLowerCase()
                                    .contains(constraint.getValue().toLowerCase());
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).reduce(Predicate::and).orElse(e -> true);
        }

        private static Comparator<Gasto> comparator(CrudFilter filter) {
            // For RDBMS just generate an ORDER BY clause
            return filter.getSortOrders().entrySet().stream().map(sortClause -> {
                try {
                    Comparator<Gasto> comparator = Comparator.comparing(
                            gasto -> (Comparable) valueOf(sortClause.getKey(),
                                    gasto));

                    if (sortClause.getValue() == SortDirection.DESCENDING) {
                        comparator = comparator.reversed();
                    }

                    return comparator;

                } catch (Exception ex) {
                    return (Comparator<Gasto>) (o1, o2) -> 0;
                }
            }).reduce(Comparator::thenComparing).orElse((o1, o2) -> 0);
        }

        private static Object valueOf(String fieldName, Gasto gasto) {
            try {
                Field field = Gasto.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(gasto);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public void persist(Gasto item) {
            if (item.getId() == null) {
                item.setId(DATABASE.stream().map(Gasto::getId).max(naturalOrder())
                        .orElse(0) + 1);
            }

            final Optional<Gasto> existingItem = find(item.getId());
            if (existingItem.isPresent()) {
                int position = DATABASE.indexOf(existingItem.get());
                DATABASE.remove(existingItem.get());
                DATABASE.add(position, item);
                Gasto gasto = GastoDao.buscarGastoPorId(item.getId());
                GastoDao.atualizarGasto(gasto);
                Notification notification = Notification.show("Gasto atualizado com sucesso!",
                        5000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                DATABASE.add(item);
                GastoDao.adicionarGasto(item);
                Notification notification = Notification.show("Gasto adicionado com sucesso!",
                        5000, Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            }
        }

        Optional<Gasto> find(Integer id) {
            return DATABASE.stream().filter(entity -> entity.getId().equals(id))
                    .findFirst();
        }

        public void delete(Gasto item) {
            DATABASE.removeIf(entity -> entity.getId().equals(item.getId()));
            GastoDao.deletarGasto(item.getId());
            Notification notification = Notification.show("Gasto removido com sucesso!",
                    5000, Notification.Position.TOP_END);
            notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        }
}
