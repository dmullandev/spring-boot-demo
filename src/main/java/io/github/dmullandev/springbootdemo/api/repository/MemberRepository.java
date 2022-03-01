package io.github.dmullandev.springbootdemo.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.github.dmullandev.springbootdemo.api.model.Member;

public interface MemberRepository extends MongoRepository<Member, String> {
}