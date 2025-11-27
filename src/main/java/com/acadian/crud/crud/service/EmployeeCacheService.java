package com.acadian.crud.crud.service;

import com.acadian.crud.crud.entity.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeCacheService {

    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
    private final Duration employeeCacheTTL;

    private ReactiveValueOperations<String, Object> ops() {
        return reactiveRedisTemplate.opsForValue();
    }

    private String key(Long id) {
        return "employee::" + id;
    }

    public Mono<Employee> get(Long id) {
        return ops().get(key(id))
                .cast(Employee.class);
    }

    public Mono<Employee> put(Employee employee) {
        return ops().set(key(employee.getId()), employee, employeeCacheTTL)
                .thenReturn(employee);
    }

    public Mono<Void> delete(Long id) {
        return ops().delete(key(id)).then();
    }

}
