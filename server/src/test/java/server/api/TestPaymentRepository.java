package server.api;

import commons.Event;
import commons.Payment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.EventRepository;
import server.database.PaymentRepository;

import java.util.ArrayList;
import java.util.*;
import java.util.function.Function;

public class TestPaymentRepository implements PaymentRepository {
    private List<Payment> payments = new ArrayList<>();
    private List<String> calledMethods = new ArrayList<>();
    private int idCounter = 1;

    private void call(String name) {
        calledMethods.add(name);
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Payment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Payment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Payment getOne(Long aLong) {
        return null;
    }

    @Override
    public Payment getById(Long aLong) {
        return null;
    }

    @Override
    public Payment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Payment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Payment> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Payment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Payment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Payment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Payment, R> R findBy(Example<S> example,
                                           Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Payment save(Payment entity) {
        entity.id = idCounter++;
        payments.add(entity);
        return entity;
    }

    @Override
    public <S extends Payment> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return payments.stream()
            .filter(payment -> id.equals(payment.id))
            .findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        return payments.stream().anyMatch(payment-> id.equals(payment.id));
    }

    @Override
    public List<Payment> findAll() {
        calledMethods.add("findAll");
        return payments;
    }

    @Override
    public List<Payment> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return payments.size();
    }

    @Override
    public void deleteById(Long id) {
        payments.removeIf(payment -> id.equals(payment.id));
    }

    @Override
    public void delete(Payment entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Payment> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Payment> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return null;
    }
}
