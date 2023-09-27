package com.sparta.miniproject.comment;

import com.sparta.miniproject.common.dto.CodeResponseDto;
import com.sparta.miniproject.common.exception.JobException;
import com.sparta.miniproject.common.exception.MessageSourceUtil;
import com.sparta.miniproject.common.util.SecurityUtil;
import com.sparta.miniproject.company.CompanyFixture;
import com.sparta.miniproject.company.CompanyRepository;
import com.sparta.miniproject.member.MemberFixture;
import com.sparta.miniproject.member.entity.Member;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final CompanyRepository companyRepository = mock(CompanyRepository.class);
    private final MessageSourceUtil source = mock(MessageSourceUtil.class);
    private final CommentService commentService = new CommentService(commentRepository, companyRepository, source);

    private static MockedStatic<SecurityUtil> securityContextHolder;

    @BeforeAll
    public static void beforeALl() {
        securityContextHolder = mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void afterAll() {
        securityContextHolder.close();
    }

    @Test
    @DisplayName("[정상 작동] 게시글 ID와 comment가 주어지면 저장된다.")
    public void create() {
        // given
        Long companyId = 1L;
        String comment = "댓글 내용";

        CommentCreateRequestDto request = new CommentCreateRequestDto();
        request.setComment(comment);

        Comment saved = CommentFixture.case1(
                CompanyFixture.case1(),
                MemberFixture.case1()
        );

        when(commentRepository.save(any())).thenReturn(
                saved
        );
        when(companyRepository.findById(any())).thenReturn(
                Optional.of(CompanyFixture.case1())
        );

        // when
        CommentResponseDto result = commentService.create(companyId, request);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("[비정상 작동] 존재하지 않는 게시글 ID가 주어지면 오류 발생.")
    public void createOccurErrorWhenNonExistedId() {
        // given
        Long companyId = 1L;
        String comment = "댓글 내용";

        CommentCreateRequestDto request = new CommentCreateRequestDto();
        request.setComment(comment);

        when(companyRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        // when
        Executable func = () -> commentService.create(companyId, request);

        // then
        assertThrows(JobException.class, func);
    }

    @Test
    @DisplayName("[정상 작동] 게시글 ID가 주어지고 수정 내용이 주어지면 수정한다.")
    public void update() {
        // given
        Long companyId = 1L;
        String comment = "댓글 내용";

        Member member = MemberFixture.case1();

        CommentUpdateRequestDto request = new CommentUpdateRequestDto();
        request.setComment(comment);

        when(commentRepository.findById(any())).thenReturn(
                Optional.of(CommentFixture.case1(
                        CompanyFixture.case1(),
                        member
                ))
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.of(member)
        );

        // when
        CommentResponseDto result = commentService.update(companyId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getComment()).isEqualTo(comment);
    }

    @Test
    @DisplayName("[비정상 작동] 존재하지 않는 댓글 수정.")
    public void updateOccurErrorWhenNonExistedComment() {
        // given
        Long companyId = 1L;
        String comment = "댓글 내용";

        Member member = MemberFixture.case1();

        CommentUpdateRequestDto request = new CommentUpdateRequestDto();
        request.setComment(comment);

        when(commentRepository.findById(any())).thenReturn(
                Optional.empty()
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.of(member)
        );

        // when
        Executable result = () -> commentService.update(companyId, request);

        // then
        assertThrows(JobException.class, result);
    }

    @Test
    @DisplayName("[비정상 작동] 비로그인 유저가 댓글 수정.")
    public void updateOccurErrorWhenNotLoggedIn() {
        // given
        Long companyId = 1L;
        String comment = "댓글 내용";

        Member member = MemberFixture.case1();

        CommentUpdateRequestDto request = new CommentUpdateRequestDto();
        request.setComment(comment);

        when(commentRepository.findById(any())).thenReturn(
                Optional.of(CommentFixture.case1(
                        CompanyFixture.case1(),
                        member
                ))
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.empty()
        );

        // when
        Executable result = () -> commentService.update(companyId, request);

        // then
        assertThrows(JobException.class, result);
    }

    @Test
    @DisplayName("[비정상 작동] 작성자가 아닌 유저가 댓글 수정.")
    public void updateOccurErrorWhenNotOwnerTry() {
        // given
        Long companyId = 1L;
        String comment = "댓글 내용";

        Member member = MemberFixture.case1();
        Member otherMember = MemberFixture.case2();

        CommentUpdateRequestDto request = new CommentUpdateRequestDto();
        request.setComment(comment);

        when(commentRepository.findById(any())).thenReturn(
                Optional.of(CommentFixture.case1(
                        CompanyFixture.case1(),
                        member
                ))
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.of(otherMember)
        );

        // when
        Executable result = () -> commentService.update(companyId, request);

        // then
        assertThrows(JobException.class, result);
    }

    @Test
    @DisplayName("[정상 작동] 게시글 ID가 주어지면 게시글을 삭제한다.")
    public void delete() {
        // given
        Long companyId = 1L;

        Member member = MemberFixture.case1();

        when(commentRepository.findById(any())).thenReturn(
                Optional.of(CommentFixture.case1(
                        CompanyFixture.case1(),
                        member
                ))
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.of(member)
        );

        // when
        CodeResponseDto result = commentService.deleteById(companyId);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("[비정상 작동] 비로그인 유저가 댓글 삭제.")
    public void deleteOccurErrorWhenNotLoggedIn() {
        // given
        Long companyId = 1L;

        Member member = MemberFixture.case1();

        when(commentRepository.findById(any())).thenReturn(
                Optional.of(CommentFixture.case1(
                        CompanyFixture.case1(),
                        member
                ))
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.empty()
        );

        // when
        Executable result = () -> commentService.deleteById(companyId);

        // then
        assertThrows(JobException.class, result);
    }

    @Test
    @DisplayName("[비정상 작동] 작성자가 아닌 유저가 댓글 삭제.")
    public void deleteOccurErrorWhenNotOwnerTry() {
        // given
        Long companyId = 1L;

        Member member = MemberFixture.case1();
        Member otherMember = MemberFixture.case2();

        when(commentRepository.findById(any())).thenReturn(
                Optional.of(CommentFixture.case1(
                        CompanyFixture.case1(),
                        member
                ))
        );

        when(SecurityUtil.getMemberLoggedIn()).thenReturn(
                Optional.of(otherMember)
        );

        // when
        Executable result = () -> commentService.deleteById(companyId);

        // then
        assertThrows(JobException.class, result);
    }
}