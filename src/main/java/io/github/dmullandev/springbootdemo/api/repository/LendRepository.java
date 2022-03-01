package io.github.dmullandev.springbootdemo.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.dmullandev.springbootdemo.api.model.Book;
import io.github.dmullandev.springbootdemo.api.model.Lend;
import io.github.dmullandev.springbootdemo.api.model.LendStatus;

public interface LendRepository extends MongoRepository<Lend, String> {
	Optional<Lend> findByBookAndStatus(Book book, LendStatus status);
}