package io.github.dmullandev.springbootdemo.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.dmullandev.springbootdemo.api.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {
	Optional<Book> findByIsbn(String isbn);
}