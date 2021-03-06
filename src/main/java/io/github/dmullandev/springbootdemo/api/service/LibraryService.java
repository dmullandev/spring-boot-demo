package io.github.dmullandev.springbootdemo.api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import io.github.dmullandev.springbootdemo.api.exception.EntityNotFoundException;
import io.github.dmullandev.springbootdemo.api.model.Author;
import io.github.dmullandev.springbootdemo.api.model.Book;
import io.github.dmullandev.springbootdemo.api.model.Lend;
import io.github.dmullandev.springbootdemo.api.model.LendStatus;
import io.github.dmullandev.springbootdemo.api.model.Member;
import io.github.dmullandev.springbootdemo.api.model.MemberStatus;
import io.github.dmullandev.springbootdemo.api.model.request.AuthorCreationRequest;
import io.github.dmullandev.springbootdemo.api.model.request.BookCreationRequest;
import io.github.dmullandev.springbootdemo.api.model.request.BookLendRequest;
import io.github.dmullandev.springbootdemo.api.model.request.MemberCreationRequest;
import io.github.dmullandev.springbootdemo.api.repository.AuthorRepository;
import io.github.dmullandev.springbootdemo.api.repository.BookRepository;
import io.github.dmullandev.springbootdemo.api.repository.LendRepository;
import io.github.dmullandev.springbootdemo.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LibraryService {

	private final AuthorRepository authorRepository;
	private final MemberRepository memberRepository;
	private final LendRepository lendRepository;
	private final BookRepository bookRepository;

	public Book readBookById(String id) {
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			return book.get();
		}
		throw new EntityNotFoundException("Cant find any book under given ID");
	}

	public List<Book> readBooks() {
		return bookRepository.findAll();
	}

	public Book readBook(String isbn) {
		Optional<Book> book = bookRepository.findByIsbn(isbn);
		if (book.isPresent()) {
			return book.get();
		}
		throw new EntityNotFoundException("Cant find any book under given ISBN");
	}

	public Book createBook(BookCreationRequest book) {
		Optional<Author> author = authorRepository.findById(book.getAuthorId());
		if (!author.isPresent()) {
			throw new EntityNotFoundException("Author Not Found");
		}
		Book bookToCreate = new Book();
		BeanUtils.copyProperties(book, bookToCreate);
		bookToCreate.setAuthor(author.get());
		return bookRepository.save(bookToCreate);
	}

	public void deleteBook(String id) {
		bookRepository.deleteById(id);
	}

	public Member createMember(MemberCreationRequest request) {
		Member member = new Member();
		BeanUtils.copyProperties(request, member);
		member.setStatus(MemberStatus.ACTIVE);
		return memberRepository.save(member);
	}

	public Member updateMember(String id, MemberCreationRequest request) {
		Optional<Member> optionalMember = memberRepository.findById(id);
		if (!optionalMember.isPresent()) {
			throw new EntityNotFoundException("Member not present in the database");
		}
		Member member = optionalMember.get();
		member.setLastName(request.getLastName());
		member.setFirstName(request.getFirstName());
		return memberRepository.save(member);
	}

	public Author createAuthor(AuthorCreationRequest request) {
		Author author = new Author();
		BeanUtils.copyProperties(request, author);
		return authorRepository.save(author);
	}

	public List<String> lendABook(BookLendRequest request) {

		Optional<Member> memberForId = memberRepository.findById(request.getMemberId());
		if (!memberForId.isPresent()) {
			throw new EntityNotFoundException("Member not present in the database");
		}

		Member member = memberForId.get();
		if (member.getStatus() != MemberStatus.ACTIVE) {
			throw new RuntimeException("User is not active to proceed a lending.");
		}

		List<String> booksApprovedToBurrow = new ArrayList<>();

		request.getBookIds().forEach(bookId -> {

			Optional<Book> bookForId = bookRepository.findById(bookId);
			if (!bookForId.isPresent()) {
				throw new EntityNotFoundException("Cant find any book under given ID");
			}

			Optional<Lend> burrowedBook = lendRepository.findByBookAndStatus(bookForId.get(), LendStatus.BURROWED);
			if (!burrowedBook.isPresent()) {
				booksApprovedToBurrow.add(bookForId.get().getName());
				Lend lend = new Lend();
				lend.setMember(memberForId.get());
				lend.setBook(bookForId.get());
				lend.setStatus(LendStatus.BURROWED);
				lend.setStartOn(Instant.now());
				lend.setDueOn(Instant.now().plus(30, ChronoUnit.DAYS));
				lendRepository.save(lend);
			}

		});
		return booksApprovedToBurrow;
	}
}
