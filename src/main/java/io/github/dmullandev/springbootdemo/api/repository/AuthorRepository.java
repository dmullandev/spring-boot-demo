package io.github.dmullandev.springbootdemo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.dmullandev.springbootdemo.api.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}