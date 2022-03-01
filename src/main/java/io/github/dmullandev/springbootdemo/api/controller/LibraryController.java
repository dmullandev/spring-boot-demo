package io.github.dmullandev.springbootdemo.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dmullandev.springbootdemo.api.service.LibraryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/library")
@RequiredArgsConstructor
public class LibraryController {
	private final LibraryService libraryService;
}