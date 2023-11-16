package com.ssafy.vue.board.controller;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.vue.board.model.BoardDto;
import com.ssafy.vue.board.model.BoardListDto;
import com.ssafy.vue.board.model.CommentDto;
import com.ssafy.vue.board.model.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@RestController
@RequestMapping("/board")
@Api("게시판 컨트롤러  API V1")
@Slf4j
public class BoardController {

	private BoardService boardService;

	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	@ApiOperation(value = "게시판 글작성", notes = "새로운 게시글 정보를 입력한다.")
	@PostMapping
	public ResponseEntity<?> writeArticle(
			@RequestBody @ApiParam(value = "게시글 정보.", required = true) BoardDto boardDto) {
		log.info("writeArticle boardDto - {}", boardDto);
		try {
			boardService.writeArticle(boardDto);
//			return ResponseEntity.ok().build();
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "게시판 글목록", notes = "모든 게시글의 정보를 반환한다.", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "회원목록 OK!!"), @ApiResponse(code = 404, message = "페이지없어!!"),
			@ApiResponse(code = 500, message = "서버에러!!") })
	@GetMapping
	public ResponseEntity<?> listArticle(
			@RequestParam @ApiParam(value = "게시글을 얻기위한 부가정보.", required = true) Map<String, String> map) {
		log.info("listArticle map - {}", map);
		try {
			BoardListDto boardListDto = boardService.listArticle(map);
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
			return ResponseEntity.ok().headers(header).body(boardListDto);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}

	@ApiOperation(value = "게시판 글보기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
	@GetMapping("/{articleno}")
	public ResponseEntity<BoardDto> getArticle(
			@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		log.info("getArticle - 호출 : " + articleno);
		boardService.updateHit(articleno);
		BoardDto aaa = boardService.getArticle(articleno);
		System.out.println(aaa);
		return new ResponseEntity<BoardDto>(aaa, HttpStatus.OK);
	}

	@ApiOperation(value = "수정 할 글 얻기", notes = "글번호에 해당하는 게시글의 정보를 반환한다.", response = BoardDto.class)
	@GetMapping("/modify/{articleno}")
	public ResponseEntity<BoardDto> getModifyArticle(
			@PathVariable("articleno") @ApiParam(value = "얻어올 글의 글번호.", required = true) int articleno)
			throws Exception {
		log.info("getModifyArticle - 호출 : " + articleno);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
	}

	@ApiOperation(value = "게시판 글수정", notes = "수정할 게시글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping
	public ResponseEntity<String> modifyArticle(
			@RequestBody @ApiParam(value = "수정할 글정보.", required = true) BoardDto boardDto) throws Exception {
		log.info("modifyArticle - 호출 {}", boardDto);

		boardService.modifyArticle(boardDto);
		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "게시판 글삭제", notes = "글번호에 해당하는 게시글의 정보를 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("/{articleno}")
	public ResponseEntity<String> deleteArticle(@PathVariable("articleno") @ApiParam(value = "살제할 글의 글번호.", required = true) int articleno) throws Exception {
		log.info("deleteArticle - 호출");
		boardService.deleteArticle(articleno);
		return ResponseEntity.ok().build();

	}
	
	@ApiOperation(value = "댓글수정", notes = "수정할 댓글 정보를 입력한다. 그리고 DB수정 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PutMapping("/comment")
	public ResponseEntity<String> modifyComment(
			@RequestBody @ApiParam(value = "수정할 댓글정보.", required = true) CommentDto commentDto) throws Exception {
		log.info("modifyComment - 호출 {}", commentDto);
		
		boardService.modifyComment(commentDto);
		return ResponseEntity.ok().build();
	}
	
	@ApiOperation(value = "게시판 댓글작성", notes = "게시글에 댓글을 작성한다.")
	@PostMapping("/comment")
	public ResponseEntity<?> writeComment(
			@RequestBody @ApiParam(value = "댓글 정보.", required = true) CommentDto commentDto) {
		log.info("writeComment commentDto - {}", commentDto);
		try {
			boardService.writeComment(commentDto);
//			return ResponseEntity.ok().build();
			return new ResponseEntity<Void>(HttpStatus.CREATED);
		} catch (Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@ApiOperation(value = "게시판 댓글삭제", notes = "댓글번호에 해당하는 댓글을 삭제한다. 그리고 DB삭제 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@DeleteMapping("comment/{commentNo}")
	public ResponseEntity<String> deleteComment(@PathVariable("commentNo") @ApiParam(value = "살제할 댓글의 글번호.", required = true) int commentNo) throws Exception {
		log.info("deleteComment - 호출");
		boardService.deleteComment(commentNo);
		return ResponseEntity.ok().build();

	}
	
	@ApiOperation(value = "네이버 블로그 로딩", notes = "네이버 블로그 URL을 입력받아 본문 또는 body 태그를 String으로 반환한다.", response = String.class)
	@GetMapping("/blog/{blogUrl}")
	public ResponseEntity<String> getBlogContent(
			@PathVariable("blogUrl") @ApiParam(value = "얻어올 네이버 블로그 URL", required = true) String blogUrl)
			throws Exception {
		log.info("getBlogContent - 호출 : " + blogUrl);

		String content = "\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" + 
				"\n" + 
				"\n" + 
				"<html lang=\"ko\">\n" + 
				"<head>\n" + 
				"<meta http-equiv=\"Pragma\" content=\"no-cache\" />\n" + 
				"<meta http-equiv=\"Expires\" content=\"-1\" />\n" + 
				"\n" + 
				"\n" + 
				"<meta name=\"referrer\" content=\"always\" />\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<!--[if ie]>\n" + 
				"<style type=\"text/css\">\n" + 
				"html {overflow: scroll; overflow-x: auto;}\n" + 
				"</style>\n" + 
				"<![endif]-->\n" + 
				"\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/LayoutTopCommon-280507866_https.css\" charset=\"UTF-8\" />\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/css/music/player-d3fc09e_https.css\">\n" + 
				"\n" + 
				"<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"/favicon.ico?3\" />\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				" \n" + 
				"\n" + 
				"	\n" + 
				"		<meta property=\"og:title\" content=\"강남 상가데크 제작 과정(인조 방부목 사용)\"/>\n" + 
				"\n" + 
				"		\n" + 
				"			\n" + 
				"			\n" + 
				"			<meta property=\"og:image\" content=\"https://blogthumb.pstatic.net/MjAyMzExMTRfMTAx/MDAxNjk5OTY4Nzc5MzYz.Z9zF4FxlC9d_diBEtnrK93m1m6o8vsfz1uOUY3eESWAg.W03MEsZiZsMvmrQp2dRjf48Vnnfxkjq-nvvyXRTzR7Ig.JPEG.wood-24/%B0%AD%B3%B2_%BB%F3%B0%A1%B5%A5%C5%A9_%C1%A6%C0%DB_12.jpg?type=w2\"/>\n" + 
				"			\n" + 
				"		\n" + 
				"        <meta property=\"og:description\" content=\"안녕하세요 우드24입니다 우드24를 잠깐 소개하자면요~! 1. 합성목재를 공장직영으로 판매합니다(국내생산)...\"/>\n" + 
				"\n" + 
				"		\n" + 
				"			<meta property=\"og:url\" content=\"https://blog.naver.com/wood-24/223264915610\"/>\n" + 
				"		\n" + 
				"	\n" + 
				"	\n" + 
				"\n" + 
				"<meta property=\"me:feed:serviceId\" content=\"blog\" />\n" + 
				"<style type=\"text/css\">\n" + 
				"/*<![CDATA[*/\n" + 
				"\n" + 
				"\n" + 
				"body {background-color:rgb(255, 255, 255);background-image:none;}\n" + 
				"#head-skin {background-image:url(https://blogfiles.pstatic.net/MjAyMTA0MTRfMjY2/MDAxNjE4MzY5NTkyNDgy.5PWsQ5skM1G6ZW21RT6V4GlwY8GRLEAOEvix_j6UjsEg.1tBMQXnIAmq4hRf4bPjaAyvTkzx4TyirzIlxXGPk5Osg.JPEG.wood-24/backtop?type=w3000);}\n" + 
				"\n" + 
				"\n" + 
				"#whole-border {border-width:0;border-color:transparent;border-style:0;background:transparent;}\n" + 
				"#whole-head {height:0;background:transparent;}\n" + 
				"#whole-body {padding:8px;background:transparent;}\n" + 
				"#whole-footer {height:0;background:transparent;}\n" + 
				"\n" + 
				"\n" + 
				"#wrapper .side-border {border-width:0;border-style:0;border-color:transparent;}\n" + 
				"#wrapper .side-head {height:0;background:transparent;}\n" + 
				"#wrapper .side-body {padding:0;background:transparent;}\n" + 
				"#wrapper .side-footer {height:0;background:transparent;}\n" + 
				"\n" + 
				"\n" + 
				"#wrapper .cm-col1 {color:rgb(108, 108, 108);}\n" + 
				"#wrapper .cm-arw {background:rgb(108, 108, 108);}\n" + 
				"#wrapper div.line {border-top-color:rgb(108, 108, 108);}\n" + 
				"#wrapper .cm-border {border-width:0px;border-style:solid;border-color:rgb(0, 0, 0);}\n" + 
				"#wrapper .cm-head {height:30px;background:url(https://blogimgs.pstatic.net/nblog/skins/infobox/1005_head.gif);}\n" + 
				"#wrapper .cm-body {background:url(https://blogimgs.pstatic.net/nblog/skins/infobox/1005_body.gif);}\n" + 
				"#wrapper .cm-footer {height:6px;background:url(https://blogimgs.pstatic.net/nblog/skins/infobox/1005_footer.gif);}\n" + 
				"#wrapper .cm-icol {color:rgb(108, 108, 108);}\n" + 
				"#wrapper h3 {margin-left:14px;bottom:4px;}\n" + 
				"#wrapper .cmore {bottom:4px;}\n" + 
				"#blog-category .listimage {background-image:url(https://blogimgs.pstatic.net/nblog/skins/infobox/0003_arw.gif);}\n" + 
				"#wrapper .buddyup {background-image:url(https://blogimgs.pstatic.net/nblog/skins/infobox/0003_icon_up.gif);}\n" + 
				"#wrapper .buddydw {background-image:url(https://blogimgs.pstatic.net/nblog/skins/infobox/0003_icon_down.gif);}\n" + 
				"#blog-category .f_icoclosed {background-image:url(https://blogimgs.pstatic.net/nblog/skins/infobox/ico_f_closed_l.gif) !important;}\n" + 
				"#blog-category .f_icoopen {background-image:url(https://blogimgs.pstatic.net/nblog/skins/infobox/ico_f_open_l.gif) !important;}\n" + 
				"#blog-category ul li.depth2 {background-image:url(https://blogimgs.pstatic.net/nblog/skins/infobox/ico_depth2_l.gif) !important;}\n" + 
				"\n" + 
				"\n" + 
				"#blog-gnb .link {color:rgb(0, 0, 0);}\n" + 
				"#blog-gnb .gnb_name {color:rgb(0, 0, 0);}\n" + 
				"#blog-gnb .bar {background:rgb(153, 153, 153);}\n" + 
				"#blog-gnb .btn_blog_login {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_login.png) no-repeat 0 0;}\n" + 
				"#blog-gnb .gnb_icon {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_menu.png) no-repeat 0 0 !important;}\n" + 
				"#blog-gnb .go-down {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_down.png);}\n" + 
				"#blog-gnb .gnb_my_namebox {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_down.png) no-repeat 100% 50% !important;}\n" + 
				"#blog-gnb .go-up {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_up.png);}\n" + 
				"#blog-gnb .gnb_lyr_opened .gnb_my_namebox {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_up.png) no-repeat 100% 50% !important;}\n" + 
				"\n" + 
				"* html #blog-gnb .btn_blog_login { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_login.png,sizingMethod=crop); }\n" + 
				"* html #blog-gnb .gnb_icon { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_menu.png,sizingMethod=crop); }\n" + 
				"* html #blog-gnb .go-down { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_down.png,sizingMethod=crop); }\n" + 
				"* html #blog-gnb .gnb_my_namebox { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_down.png,sizingMethod=crop); }\n" + 
				"* html #blog-gnb .go-up { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_up.png,sizingMethod=crop); }\n" + 
				"* html #blog-gnb .gnb_lyr_opened .gnb_my_namebox { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_up.png,sizingMethod=crop); }\n" + 
				"\n" + 
				"#foldset .go-left {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_left.png)}\n" + 
				"#foldset .go-right {background:url(https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_right.png)}\n" + 
				"\n" + 
				"* html #foldset .go-left { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_left.png,sizingMethod=crop); }\n" + 
				"* html #foldset .go-right { background:transparent; filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=https://blogimgs.pstatic.net/nblog/skins/gnb2014/1007_right.png,sizingMethod=crop); }\n" + 
				"\n" + 
				"#body {width:789px; margin:0 auto; padding:0;}\n" + 
				"body, #head-skin, #bottom-skin {background-position:50% 0;}\n" + 
				"#wrapper {width:773px;}\n" + 
				"#twocols {width:773px; display:inline; float:left;}\n" + 
				"#content-area {width:594px; display:inline; float:right;}\n" + 
				"#left-area {float:left;}\n" + 
				".side-width-1 {width:171px;  display:inline;}\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				".itemfont, a.itemfont:link, a.itemfont:visited, a.itemfont:hover, a.itemfont:active {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"a.itemTagfont:link, a.itemTagfont:visited {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"a.itemTagfont:hover , a.itemTagfont:active {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial; color:#418F21;}\n" + 
				".itemSubjectfont   {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"#post-area .post_adpost .itemSubjectfont {font-size:10pt !important;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"\n" + 
				"\n" + 
				"#blog-title {height:600px;background:transparent;}\n" + 
				"#blogTitleText {vertical-align:bottom;text-align:center;}\n" + 
				"#blogTitleName {display:inline;color:rgb(255, 255, 255);font-family:\"바른고딕\", NanumBarunGothic, NanumBarunGothicWebFont, \"NanumBarun Gothic\", \"맑은 고딕\", Dotum, \"돋움\", Helvetica, \"Apple SD Gothic Neo\", sans-serif;font-size:30px;}\n" + 
				"\n" + 
				"#blog-menu .border {height:36px;border-width:0px;border-style:solid;border-color:#000000;}\n" + 
				"#blog-menu table {height:36px;background:url(https://blogimgs.pstatic.net/nblog/skins/menu/0134_773.gif);}\n" + 
				"#blog-menu .on {color:#FFEF34;}\n" + 
				"#blog-menu .off {color:#FFFFFF;}\n" + 
				"#blog-menu .bar {background:#FFFFFF;}\n" + 
				"\n" + 
				"#blog-profile .border {border-width:0px;border-style:solid;border-color:rgb(255, 255, 255);}\n" + 
				"#blog-profile .bg-head {height:8px;background:url(https://blogimgs.pstatic.net/nblog/skins/profile/0148_head.gif);}\n" + 
				"#blog-profile .bg-body {padding:0;background:url(https://blogimgs.pstatic.net/nblog/skins/profile/0148_body.gif);}\n" + 
				"#blog-profile .bg-footer {height:6px;background:url(https://blogimgs.pstatic.net/nblog/skins/profile/0148_footer.gif);}\n" + 
				"#blog-profile .col {color:rgb(138, 131, 126);}\n" + 
				"#blog-profile .icon {background-image:url(https://blogimgs.pstatic.net/nblog/skins/profile/0148_icon.gif);}\n" + 
				"#blog-profile .image {display:block;}\n" + 
				"#blog-profile .image img {width:155px;}\n" + 
				"#blog-profile .align {text-align:left;}\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"#blog-rss {height:10px;background-image:url(https://blogimgs.pstatic.net/nblog/skins/rss/0102_image.gif);}\n" + 
				"#blog-rss .rss1 {width:52px;height:10px;margin:0 0 0 10px;}\n" + 
				"#blog-rss .rss2 {width:48px;height:10px;margin:0 0 0 65px;}\n" + 
				"#blog-rss .rss3 {width:43px;height:10px;margin:0 0 0 118px;}\n" + 
				"\n" + 
				"#post-area .post {border-width:0px;border-style:solid;border-color:rgb(255, 255, 255);}\n" + 
				"#post-area .post-back {background:rgb(255, 255, 255);}\n" + 
				"#post-area .pcol1 {color:rgb(85, 122, 116);font-size:18px;}\n" + 
				"#post-area .pcol2 {color:rgb(85, 122, 116);}\n" + 
				"#post-area .pcol2b {background:rgb(157, 175, 170);}\n" + 
				"#post-area .cline {border-bottom-color:rgb(157, 175, 170);}\n" + 
				"#post-area .pcol3 {color:rgb(255, 78, 0);}\n" + 
				"#post-area .dline {border-top-width:1px;border-top-style:solid;border-top-color:rgb(85, 122, 116);}\n" + 
				"#post-area .htl {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .htc {background:transparent;height:15px;}\n" + 
				"#post-area .htr {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .ftl {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .ftc {background:transparent;height:15px;}\n" + 
				"#post-area .ftr {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .bcl {background:transparent;width:15px;}\n" + 
				"#post-area .bcc {background:transparent;}\n" + 
				"#post-area .bcr {background:transparent;width:15px;}\n" + 
				"#post-area .ico1 {background:none;}\n" + 
				"#post-area .ico2 {background:none;}\n" + 
				"#post-area .ico3 {background:none;}\n" + 
				"#post-area .comment {background:transparent;}\n" + 
				"#post-area .btn {background:url(https://blogimgs.pstatic.net/nblog/skins/poststyle/0113_input.gif);}\n" + 
				"#post-area .btn-sympathy {background:url(https://blogimgs.pstatic.net/nblog/skins/poststyle/btn_sympathy_02.gif);}\n" + 
				"#post-area .btn_r {background:url(https://blogimgs.pstatic.net/nblog/skins/poststyle/btn_reply_0113.gif);}\n" + 
				"\n" + 
				".post-back {background:rgb(255, 255, 255);}\n" + 
				".pcol1 {color:rgb(85, 122, 116);font-size:18px;}\n" + 
				".pcol2 {color:rgb(85, 122, 116);}\n" + 
				".pcol2b {background:rgb(157, 175, 170);}\n" + 
				".pcol3 {color:rgb(255, 78, 0);}\n" + 
				".bcc {background:transparent;}\n" + 
				"#post-area .sline {border-bottom-color:rgb(85, 122, 116);}\n" + 
				"\n" + 
				"\n" + 
				"#body {width:789px;}\n" + 
				"#wrapper {width:773px;}\n" + 
				"#twocols {width:773px;}\n" + 
				"#content-area {width:594px;}\n" + 
				".side-width-1 { display:inline;}\n" + 
				".side-width-2 { display:none;}\n" + 
				"\n" + 
				"	\n" + 
				"	\n" + 
				"/*]]>*/\n" + 
				"</style>\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/global/RemoveSubDomain-12d2f16_https.js\"></script>\n" + 
				"<script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var mylogURL = 'https://blog.naver.com';\n" + 
				"var adminURL = 'https://admin.blog.naver.com';\n" + 
				"var noteURL = 'https://note.naver.com';\n" + 
				"var itemURL = 'https://item.naver.com';\n" + 
				"var cafeURL = 'https://cafe.naver.com';\n" + 
				"var profileURL = 'https://blog.naver.com';\n" + 
				"var blogURL = 'https://blog.naver.com';\n" + 
				"var blogURLHttp = 'https://blog.naver.com';\n" + 
				"var prologueURL = 'https://blog.naver.com';\n" + 
				"var sectionURL = 'https://section.blog.naver.com';\n" + 
				"var memologURL = 'https://blog.naver.com';\n" + 
				"var lifeURL = 'https://lifelog.blog.naver.com';\n" + 
				"var uploadURL = 'https://upload.blog.naver.com';\n" + 
				"var imageURL = 'https://blogimgs.pstatic.net';\n" + 
				"var attachURL = 'https://blogfiles.pstatic.net';\n" + 
				"var thumbnailURL = 'https://blogthumb.pstatic.net';\n" + 
				"var nonCachedThumbnailURL = 'https://blogthumb.pstatic.net';\n" + 
				"var filetransURL = 'http://filetrans.blog.naver.com';\n" + 
				"var item2_bridgeURL = 'http://bridge.item2.naver.com';\n" + 
				"var idURL = 'http://nid.naver.com';\n" + 
				"var idURLSecret = 'https://nid.naver.com';\n" + 
				"var mapviewURL = 'http://mapview.naver.com';\n" + 
				"var exifURL = 'https://exif-blog.ssl.phinf.net';\n" + 
				"var viewerImageUrl = imageURL + \"/blog20/blog/layout_photo/viewer/\";\n" + 
				"var domainURL = \"\";\n" + 
				"var greenReviewCampaignURL = 'http://campaign.naver.com';\n" + 
				"var writeURL = 'https://blog.naver.com';\n" + 
				"var spiURL = 'https://ssl.pstatic.net/spi';\n" + 
				"var blogProfileThumbnailUrl='https://blogpfthumb-phinf.pstatic.net';\n" + 
				"var naverCommentStaticURL = 'https://ssl.pstatic.net/static.cbox';\n" + 
				"var naverCommentApiURL = 'https://apis.naver.com/commentBox/cbox9';\n" + 
				"var naverCommentApiProxyURL = 'https://apis.naver.com/commentBox/blogid';\n" + 
				"var itemImagesURL = 'https://blogimgs.pstatic.net/item';\n" + 
				"\n" + 
				"var sCssUrlForPhotoLayer = 'https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/css/storyphoto_original_viewer-adf5d27_https.css';\n" + 
				"var commonStatDailyURL = 'https://blog.stat.naver.com';\n" + 
				"var lcs_SerName = 'https://lcs.naver.com';\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var blogId = 'wood-24' || 'wood-24';  \n" + 
				"var userId = ''; \n" + 
				"var userNo = ''; \n" + 
				"var userNaverId = '';\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var isWritingBlocked = false;\n" + 
				"var isWritingBlockedBySpamIP = false;\n" + 
				"var isWritingBlockedBySpamIPManually = false;\n" + 
				"var isLoggedIn = false;\n" + 
				"var isShortestContentAreaWidth = false;\n" + 
				"\n" + 
				"var blogOwner = false; \n" + 
				"\n" + 
				"var blogNo = '131720049';\n" + 
				"var nickName = '우드24';\n" + 
				"var isInitializingBlog = false;\n" + 
				"var rightClickOpenYn = true;\n" + 
				"var isMylogBlocked = false;\n" + 
				"var isBlogBlock = false;\n" + 
				"var blockOrWriteBlock = false;\n" + 
				"var autoSourcingYn = false;\n" + 
				"\n" + 
				"\n" + 
				"var isMemolog = false;\n" + 
				"var isMemologList = false;\n" + 
				"var isPrologue = false;\n" + 
				"\n" + 
				"\n" + 
				"var isBlogMe = false;\n" + 
				"\n" + 
				"var isUsingUrl = false;\n" + 
				"\n" + 
				"\n" + 
				"var isRequestFromIPad = false;\n" + 
				"\n" + 
				"\n" + 
				"var isOverIOS5_0Version = false;\n" + 
				"var isOverAndroidJellybeanVersion = false;\n" + 
				"var isCommingNaverApp = false;\n" + 
				"var isUsingNaverKitkatEngine = false;\n" + 
				"var isAndroidKitkatVersion = false;\n" + 
				"\n" + 
				"var defaultSmartEditorVersion = 2;\n" + 
				"\n" + 
				"var adContentLocation = \"0\";\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var readOnlyStatus = false;\n" + 
				"var readOnlyStatusAll = false;\n" + 
				"var readOnlyLinkErrorMsg = '죄송합니다. 블로그 서비스 점검 중입니다. \\n점검 시간 동안은 기능 이용이 제한됩니다. \\n자세한 내용은 블로그 홈 공지사항을 참고해주세요.';\n" + 
				"var readOnlyCommentMsg = '12월 17일(월) 오전 0시~8시 서비스 점검으로 댓글 작성이 제한됩니다. \\n더욱 안정적인 서비스로 찾아뵙겠습니다. 불편하시더라도 조금만 기다려주세요.';\n" + 
				"var sIsTagReadOnly = '';\n" + 
				"var sTagSorryText = '죄송합니다. 태그 서비스 점검 중입니다.\\n점검 시간 동안은 일부 서비스 이용이 제한됩니다.\\n자세한 내용은 공지사항을 참조하세요.';\n" + 
				"var blogQuickPostingUseType = \"NOT_SET\"; \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var isCommentReadOnly = false;\n" + 
				"var sCommentReadOnlyBlockSubmit = '죄송합니다. 블로그 댓글, 답글 서비스 점검 중에는 이용이 제한됩니다.';\n" + 
				"var sCommentReadOnlyWriteMsg = '서비스 점검으로 댓글 작성이 제한됩니다.\\n더욱 안정적인 서비스로 찾아뵙겠습니다. 불편하시더라도 조금만 기다려주세요.';\n" + 
				" \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var isJeagleDebug = false;\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var skinId = \"0\";\n" + 
				"var skinType = \"C\";\n" + 
				"\n" + 
				"var isEnglish = \"false\";\n" + 
				"\n" + 
				"var areaCode = \"11B10101\";\n" + 
				"var weatherType = \"0\";\n" + 
				"\n" + 
				"var currencySign = \"ALL\";\n" + 
				"\n" + 
				"var listNumVisitor = '5';\n" + 
				"var isVisitorOpen = true;\n" + 
				"var isCategoryOpen = true;\n" + 
				"var writingMaterialListType = '1'\n" + 
				"var previewCalType = '';\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var oExternalWidget = [{'teminationMessageHtml':'','blockYn':false,'blogId':'wood-24','reversedWidgetCode':'E3%F2%+22%4808049658461_dne_kramkoob_yksuh22%D3%di+napsC3%E3%F2%+22%4808049658461_trats_kramkoob_yksuh22%D3%di+napsC3%E3%aF2%C3%E3%F2%+22%B3%xp006A3%thgiehr+B3%xp071A3%htdiwr+B3%xp006A3%thgieh+B3%xp071A3%htdiw+22%D3%elyts+22%22%D3%ngila+22%D752%B752%22%D3%eulavnosj+22%gnp.0_tejdiw22%D3%eltit+22%eurt22%D3%eqgmi+22%00622%D3%thgiehr+22%07122%D3%htdiwr+22%00622%D3%thgieh+22%07122%D3%htdiw+22%otohp22%D3%epytbus_s+22%tnemhcatta22%D3%epyt_s+22%tcejbo_es__22%D3%ssalc+22%gnp.0_tejdiwF2%42-doow.GNP.ggP4a2HF9wdSQkba9WbxQvYY-gJvNLOlYEzVv3TrIXBy.g8f42Vyj_fp3YJ9v-AwDBeYyAyQ39XtVVZjCGGYd1sWw.zMTO3ADN5YTN4QjNxADMF2%xgjMfBzMzAjMyAjMF2%ten.citatsp.selifgolbF2%F2%A3%sptth22%D3%crs+22%946598708049658461_tcejbo_es22%D3%di+gmiC3%E3%22%fles_22%D3%tegrat+22%42doowF2%moc.revan.erotstramsF2%F2%A3%sptth22%D3%ferh+22%knalb_22%D3%tegrat+aC3%','serviceTerminated':false,'seq':7},{'teminationMessageHtml':'','blockYn':false,'blogId':'wood-24','reversedWidgetCode':'E3%napsF2%C3%E3%F2%+22%9667567658461_dne_kramkoob_yksuh22%D3%di+napsC3%E3%22%9667567658461_trats_kramkoob_yksuh22%D3%di+napsC3%E3%F2%+22%B3%xp69A3%thgiehr+B3%xp071A3%htdiwr+B3%xp69A3%thgieh+B3%xp071A3%htdiw+22%D3%elyts+22%22%D3%ngila+22%D752%B752%22%D3%eulavnosj+22%gnp.2_tejdiw22%D3%eltit+22%eurt22%D3%eqgmi+22%6922%D3%thgiehr+22%07122%D3%htdiwr+22%6922%D3%thgieh+22%07122%D3%htdiw+22%otohp22%D3%epytbus_s+22%tnemhcatta22%D3%epyt_s+22%tcejbo_es__22%D3%ssalc+22%gnp.2_tejdiwF2%42-doow.GNP.gkjCsbXTPegBs8woesEmmoYqfzgq-ziN9vLzXEI6nZ1Q.g8j__RiFeiqRa0q5XQ-oFfgGLGPgEwykHNxMVFsWCX4m.yETN3UjN3YTN4QjNxADMF2%4UjMfBzMzAjMyAjMF2%ten.citatsp.selifgolbF2%F2%A3%sptth22%D3%crs+22%291332667567658461_tcejbo_es22%D3%di+gmiC3%','serviceTerminated':false,'seq':8},{'teminationMessageHtml':'','blockYn':false,'blogId':'wood-24','reversedWidgetCode':'E3%napsF2%C3%E3%F2%+22%1714177658461_dne_kramkoob_yksuh22%D3%di+napsC3%E3%22%1714177658461_trats_kramkoob_yksuh22%D3%di+napsC3%E3%aF2%C3%E3%F2%+22%B3%xp491A3%thgiehr+B3%xp071A3%htdiwr+B3%xp491A3%thgieh+B3%xp071A3%htdiw+22%D3%elyts+22%22%D3%ngila+22%D752%B752%22%D3%eulavnosj+22%gnp.3_tejdiw22%D3%eltit+22%eurt22%D3%eqgmi+22%49122%D3%thgiehr+22%07122%D3%htdiwr+22%49122%D3%thgieh+22%07122%D3%htdiw+22%otohp22%D3%epytbus_s+22%tnemhcatta22%D3%epyt_s+22%tcejbo_es__22%D3%ssalc+22%gnp.3_tejdiwF2%42-doow.GNP.gwWJz3wV0AGuJ8aFgqJ-uMFG7mv5J_3z5qQLLZXRh7zh.gEAH5HdVnOjAxI-yo_YW1YnlWUO4dqvGdLPJgCKj3jt5.2kTOzEzN3YTN4QjNxADMF2%zIjMfBzMzAjMyAjMF2%ten.citatsp.selifgolbF2%F2%A3%sptth22%D3%crs+22%738365614177658461_tcejbo_es22%D3%di+gmiC3%E3%22%42doowF2%moc.revan.erotstramsF2%F2%A3%sptth22%D3%ferh+22%fles_22%D3%tegrat+22%knalb_22%D3%tegrat+aC3%','serviceTerminated':false,'seq':9},{'teminationMessageHtml':'','blockYn':false,'blogId':'wood-24','reversedWidgetCode':'E3%F2%+22%3246059658461_dne_kramkoob_yksuh22%D3%di+napsC3%E3%F2%+22%3246059658461_trats_kramkoob_yksuh22%D3%di+napsC3%E3%aF2%C3%E3%F2%+22%B3%xp142A3%thgiehr+B3%xp071A3%htdiwr+B3%xp142A3%thgieh+B3%xp071A3%htdiw+22%D3%elyts+22%22%D3%ngila+22%D752%B752%22%D3%eulavnosj+22%gnp.1_tejdiw22%D3%eltit+22%eurt22%D3%eqgmi+22%14222%D3%thgiehr+22%07122%D3%htdiwr+22%14222%D3%thgieh+22%07122%D3%htdiw+22%otohp22%D3%epytbus_s+22%tnemhcatta22%D3%epyt_s+22%tcejbo_es__22%D3%ssalc+22%gnp.1_tejdiwF2%42-doow.GNP.gU4c7w_LmaNx2LIJWC5N-MtCt0cyAZIjOR7uKj3Fy_2I.go0pnG1fZZvG4S8lz0XTmP65zpT9hBLBI-KXHP58Egu8.5UjM2ATN5YTN4QjNxADMF2%5cTMfBzMzAjMyAjMF2%ten.citatsp.selifgolbF2%F2%A3%sptth22%D3%crs+22%549157146059658461_tcejbo_es22%D3%di+gmiC3%E3%22%fles_22%D3%tegrat+22%6643782083F2%stcudorpF2%42doowF2%moc.revan.erotstramsF2%F2%A3%sptth22%D3%ferh+22%knalb_22%D3%tegrat+aC3%','serviceTerminated':false,'seq':10}];\n" + 
				"\n" + 
				"\n" + 
				"var calType = \"\";\n" + 
				"\n" + 
				"\n" + 
				"var curLog = 'https://blog.naver.com';\n" + 
				"var playerType = '0';\n" + 
				"var playerDesign = '0';\n" + 
				"\n" + 
				"\n" + 
				"var isCommentOpen = false;\n" + 
				"var listNumComment = 5;\n" + 
				"\n" + 
				"\n" + 
				"var isBuddyOpen = false;\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var userSelectMenu = '';\n" + 
				"\n" + 
				"\n" + 
				"var visitorgpData = '';\n" + 
				"var vigitorSetUrl = '';\n" + 
				"var vigitorPropUrl = '/versioning//common/swf/Widget/visitorGP/VisitorGP-50a2c61.xml';\n" + 
				"var visitorgp_vars = {\n" + 
				"	setUrl    : vigitorSetUrl,\n" + 
				"	req_url   : '/NVisitorgp4Ajax.naver?blogId=' + blogId,\n" + 
				"	prop_url  : vigitorPropUrl,\n" + 
				"	graphType : visitorgpData,\n" + 
				"	flashvars : function(){\n" + 
				"		return \"req_url=\" + this.req_url +\"&prop_url=\" + this.prop_url +\"&graphType=\" + this.graphType;\n" + 
				"	},\n" + 
				"	height    : 121\n" + 
				"};\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var watchData = '';\n" + 
				"var watch_design = {\n" + 
				"	name    : watchData.split('|')[0],\n" + 
				"	font    : watchData.split('|')[1],\n" + 
				"	color   : watchData.split('|')[2],\n" + 
				"	bgcolor : watchData.split('|')[3]\n" + 
				"}\n" + 
				"var watch_vars = {\n" + 
				"	blogId : blogId,\n" + 
				"	skinType : skinType,\n" + 
				"	skinId : skinId\n" + 
				"}\n" + 
				"\n" + 
				"\n" + 
				"var serviceId = '';\n" + 
				"var naverId = '';\n" + 
				"var happyBeanApiURL = '';\n" + 
				"var reqparam = '';\n" + 
				"var HAPPYBEAN_API_URL = \"\";\n" + 
				"var HAPPYBEAN_FUND_URL = \"\";\n" + 
				"//var HAPPYBEAN_MY_URL   = \"\";\n" + 
				"var HAPPYBEAN_DONATION_URL = \"\";\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var selectCategoryNo = \"12\";\n" + 
				"\n" + 
				"\n" + 
				"var aNonWidget = ['rss', 'powered','adpost','buddyconnect','greenreview'];\n" + 
				"<!-- var aWidget = ['gnb', 'externalwidget','music'];  -->\n" + 
				"var aWidget = [];\n" + 
				"\n" + 
				"\n" + 
				"aWidget.push('title');\n" + 
				"\n" + 
				"aWidget.push('menu');\n" + 
				"\n" + 
				"aWidget.push('profile');\n" + 
				"\n" + 
				"aWidget.push('category');\n" + 
				"\n" + 
				"aWidget.push('search');\n" + 
				"\n" + 
				"aWidget.push('7');\n" + 
				"\n" + 
				"aWidget.push('10');\n" + 
				"\n" + 
				"aWidget.push('8');\n" + 
				"\n" + 
				"aWidget.push('9');\n" + 
				"\n" + 
				"aWidget.push('business');\n" + 
				"\n" + 
				"aWidget.push('rss');\n" + 
				"\n" + 
				"aWidget.push('content');\n" + 
				"\n" + 
				"aWidget.push('gnb');\n" + 
				"\n" + 
				"aWidget.push('externalwidget');\n" + 
				"\n" + 
				"var bIsRemocon = false;\n" + 
				"var bIsPreview = document.location.href.indexOf(\"PostPreview.nhn\") > -1 || document.location.href.indexOf(\"MemologMemoPreview.nhn\") > -1;\n" + 
				"\n" + 
				"var isTopLayoutEmpty = false;\n" + 
				"\n" + 
				"var openSideType = \"1\";\n" + 
				"var foldSideType = \"2\";\n" + 
				"\n" + 
				"var g_ContentShareObject = {\n" + 
				"	adPostPreview : false,\n" + 
				"	adPostPreviewSide : false,\n" + 
				"	adPostPreviewContent : false,\n" + 
				"	adPostAlign : \"\",\n" + 
				"	adPostCount : \"\"\n" + 
				"};\n" + 
				"\n" + 
				"var blogDomainUrl = \"\";\n" + 
				"var sAjaxFlashUrl = '/versioning//common/lib/ajax.flash/ajax-885363e.swf';\n" + 
				"var isHappyBeanLeverage = false;\n" + 
				"\n" + 
				"\n" + 
				"var doc = document.documentElement;\n" + 
				"doc.setAttribute('data-useragent',navigator.userAgent);\n" + 
				"var gbIsNotOpenBlogUser = \"false\" === \"true\";\n" + 
				"var gbIsNPayAccepted = \"false\";\n" + 
				"var gbIsPayAcceptedOrBefore = \"false\";\n" + 
				"</script>\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/Jindo152-313837964_https.js\" charset=\"utf-8\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/LayoutTopCommon-1084894008_https.js\" charset=\"UTF-8\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/JindoComponent-190469086_https.js\" charset=\"utf-8\"></script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"		<meta name=\"robots\" content=\"index,follow\"/>\n" + 
				"	\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<meta http-equiv='imagetoolbar' content='no'/>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\n" + 
				"<meta property=\"og:type\" content=\"article\"/>\n" + 
				"<meta property=\"og:article:author\" content=\"네이버 블로그 | 우드24\"/>\n" + 
				"<meta property=\"og:site_name\" content=\"네이버 블로그 | 우드24\" />\n" + 
				"<meta property=\"naverblog:nickname\" content=\"우드24\" />\n" + 
				"<meta property=\"naverblog:profile_image\" content=\"https://blogpfthumb-phinf.pstatic.net/MjAxODExMTBfODcg/MDAxNTQxODMzMjg0NzUy.D64-EQ1hrmaSPKFGT0jSt5t4EK7cFn1lZnbf-rwsSg8g.D5Dss_alDxNrB-aYWxnI0FBtVTMPuFwY-58EexTl2uQg.JPEG.wood-24/%25BB%25F3%25C8%25A3.jpg\" />\n" + 
				"<meta property=\"me2:post_body\" content=\"&quot;강남 상가데크 제작 과정(인조 방부목 사용)&quot;:https://blog.naver.com/wood-24/223264915610\"/>\n" + 
				"<meta property=\"me2:post_tag\" content=\"네이버블로그 우드24\"/>\n" + 
				"<meta property=\"me:feed:serviceId\" content=\"blog\" />\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"		<title>강남 상가데크 제작 과정(인조 방부목 사용) : 네이버 블로그</title>\n" + 
				"	\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/PostView-876979288_https.css\" charset=\"UTF-8\" />\n" + 
				"\n" + 
				"\n" + 
				"    <link href=\"https://editor-static.pstatic.net/v/basic/1.50.0/css/se.viewer.desktop.css?v=1.50.0-20231027170641\" rel=\"stylesheet\">\n" + 
				"\n" + 
				"<style type=\"text/css\">\n" + 
				"/*<![CDATA[*/\n" + 
				"\n" + 
				"#post-area .post {border-width:0px;border-style:solid;border-color:rgb(255, 255, 255);}\n" + 
				"#post-area .post-back {background:rgb(255, 255, 255);}\n" + 
				"#post-area .pcol1 {color:rgb(85, 122, 116);font-size:18px;}\n" + 
				"#post-area .pcol2 {color:rgb(85, 122, 116);}\n" + 
				"#post-area .pcol2b {background:rgb(157, 175, 170);}\n" + 
				"#post-area .cline {border-bottom-color:rgb(157, 175, 170);}\n" + 
				"#post-area .pcol3 {color:rgb(255, 78, 0);}\n" + 
				"#post-area .dline {border-top-width:1px;border-top-style:solid;border-top-color:rgb(85, 122, 116);}\n" + 
				"#post-area .htl {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .htc {background:transparent;height:15px;}\n" + 
				"#post-area .htr {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .ftl {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .ftc {background:transparent;height:15px;}\n" + 
				"#post-area .ftr {background:transparent;width:15px;height:15px;}\n" + 
				"#post-area .bcl {background:transparent;width:15px;}\n" + 
				"#post-area .bcc {background:transparent;}\n" + 
				"#post-area .bcr {background:transparent;width:15px;}\n" + 
				"#post-area .ico1 {background:none;}\n" + 
				"#post-area .ico2 {background:none;}\n" + 
				"#post-area .ico3 {background:none;}\n" + 
				"#post-area .comment {background:transparent;}\n" + 
				"#post-area .btn {background:url(https://blogimgs.pstatic.net/nblog/skins/poststyle/0113_input.gif);}\n" + 
				"#post-area .btn-sympathy {background:url(https://blogimgs.pstatic.net/nblog/skins/poststyle/btn_sympathy_02.gif);}\n" + 
				"#post-area .btn_r {background:url(https://blogimgs.pstatic.net/nblog/skins/poststyle/btn_reply_0113.gif);}\n" + 
				" \n" + 
				" \n" + 
				".itemfont, a.itemfont:link, a.itemfont:visited, a.itemfont:hover, a.itemfont:active {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"a.itemTagfont:link, a.itemTagfont:visited {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"a.itemTagfont:hover , a.itemTagfont:active {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial; color:#418F21;}\n" + 
				".itemSubjectfont   {font-size:9pt;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				"#post-area .post_adpost .itemSubjectfont {font-size:10pt !important;font-family:돋움, 굴림, seoul, verdana, arial;}\n" + 
				" \n" + 
				"/*]]>*/\n" + 
				"</style>\n" + 
				"<style></style> \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"</head>\n" + 
				"<body class=\"contw-594\">\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\">JEagleEyeClient.setEnable(isJeagleDebug);</script>\n" + 
				"\n" + 
				"    \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"floating_area_header\" class=\"floating_header\">\n" + 
				"	<div class=\"inner\">\n" + 
				"		<div class=\"area_logo\">\n" + 
				"			<a href=\"http://www.naver.com\" class=\"link_naver sp_blog\" target=\"_blank\" onclick=\"nclk_v2(this,'flt.naver','','');\">NAVER</a>\n" + 
				"			<a href=\"https://blog.naver.com\" class=\"link_blog sp_blog\" target=\"_top\" onclick=\"nclk_v2(this,'flt.blog','','');\">블로그</a>\n" + 
				"			<i class=\"icon_bar\"></i>\n" + 
				"		</div>\n" + 
				"		<div class=\"area_blog_name\">\n" + 
				"			<a href=\"https://blog.naver.com/PostList.naver?blogId=wood-24\" class=\"link_user_blog\" onclick=\"nclk_v2(this,'flt.blogname','','');\">\n" + 
				"				<strong class=\"user_blog_name\">우드24</strong>\n" + 
				"			</a>\n" + 
				"		</div>\n" + 
				"		<div class=\"area_search\" role=\"search\">\n" + 
				"			<form id=\"floatingSearchForm\" action=\"https://blog.naver.com/PostSearchList.naver\">\n" + 
				"				<fieldset>\n" + 
				"					<legend class=\"blind\">블로그 검색</legend>\n" + 
				"					<div class=\"search_box\">\n" + 
				"						<input type=\"text\" id=\"this_blog_search\" name=\"SearchText\" class=\"text_box\" title=\"이 블로그에서 검색\" autocomplete=\"off\" onclick=\"nclk_v2(this,'flt.search','','');\">\n" + 
				"						<label id=\"this_blog_search_placeholder\" for=\"this_blog_search\" class=\"label_text_box\">이 블로그에서 검색</label>\n" + 
				"						<div id=\"autocomplete_layer\" class=\"autocomplete_layer\" style=\"display: none\">\n" + 
				"							<ul class=\"list_autocomplete\">\n" + 
				"							</ul>\n" + 
				"						</div>\n" + 
				"						<input type=\"hidden\" name=\"blogId\" value=\"wood-24\"/>\n" + 
				"						<a href=\"#\" id=\"this_blog_floating_search_btn\" class=\"btn_search\" aria-label=\"검색\" role=\"button\" tabindex=\"0\" onclick=\"nclk_v2(this,'flt.searchbtn','',''); return false;\"><i class=\"icon_search sp_blog\"></i></a>\n" + 
				"					</div>\n" + 
				"				</fieldset>\n" + 
				"			</form>\n" + 
				"		</div>\n" + 
				"		<div class=\"area_category floating_area_btn_category_parent\">\n" + 
				"			<a href=\"#\" onclick=\"return false;\" class=\"btn_category floating_area_btn_category\" aria-label=\"카테고리 열기\" role=\"button\">\n" + 
				"						<span class=\"icon_category_group\">\n" + 
				"							<i class=\"line\"></i>\n" + 
				"							<i class=\"line\"></i>\n" + 
				"							<i class=\"line\"></i>\n" + 
				"						</span>\n" + 
				"				<i class=\"icon_category sp_blog\"></i>\n" + 
				"			</a>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"floating_side_area_info\" class=\"floating_category\">\n" + 
				"\n" + 
				"</div>\n" + 
				"<div id=\"floating_inf\"></div>\n" + 
				"\n" + 
				"	\n" + 
				"		<div id=\"floating_bottom\" class=\"floating_bottom\">\n" + 
				"			<div class=\"inner\">\n" + 
				"				<div class=\"wrap_postcomment\" style=\"z-index: 1001\">\n" + 
				"					\n" + 
				"						<div class=\"area_sympathy\">\n" + 
				"							<!-- [D]  좋아요 시스템 -->\n" + 
				"							<div class=\"u_likeit_list_module\">\n" + 
				"								<!-- [D] 공감 아이콘이 더블 하트일 경우 u_likeit_list_btn에 double_heart 클래스 추가 -->\n" + 
				"								<div class=\"u_likeit_list_module _reactionModule\" data-sid=\"BLOG\" data-did=\"BLOG\" data-cid=\"wood-24_223264915610\" data-catgid=\"post\">\n" + 
				"									<a href=\"#\" onclick=\"return false;\" class=\"u_likeit_list_btn _button _param(223264915610)\" data-type=\"like\" data-log=\"reb.llike|reb.lunlike\" data-isHiddenCount=\"true\" role=\"button\">\n" + 
				"										<span class=\"u_ico _icon\"></span>\n" + 
				"										<em class=\"u_cnt _count\"></em>\n" + 
				"									</a>\n" + 
				"								</div>\n" + 
				"							</div>\n" + 
				"							<!-- //[D]  좋아요 시스템 -->\n" + 
				"							<a href=\"#\" onclick=\"nclk_v2(this,'reb.symopereb.symopenn','','');return false;\" role=\"button\" class=\"btn_arr _floating_sympathy_history _param(3|1|223264915610|0)\">\n" + 
				"								<div class=\"u_likeit_list_module _reactionModule\" data-sid=\"BLOG\" data-did=\"BLOG\" data-cid=\"wood-24_223264915610\" data-catgid=\"post\" data-isHiddenCount=\"true\" data-isUsedLabelAsZeroCount=\"false\">\n" + 
				"									<span class=\"u_likeit_list_btn _button\" data-type=\"like\" data-log=\"pst.flike|pst.funlike\" role=\"button\" data-isUsedLabelAsZeroCount=\"true\">\n" + 
				"										<em>공감</em><em class=\"u_txt _label\">해요</em>\n" + 
				"										<em class=\"u_cnt _count\"></em>\n" + 
				"									</span>\n" + 
				"								</div>\n" + 
				"							</a>\n" + 
				"						</div>\n" + 
				"					\n" + 
				"					\n" + 
				"						<div class=\"area_comment\">\n" + 
				"							<a href=\"#\" role=\"button\" class=\"btn_comment _floating_bottom_btn_comment _param(1|1|223264915610|0|true)\" id=\"btn_comment_2\" onclick=\"nclk_v2(this,'reb.replyopen','','');return false;\">\n" + 
				"								<i class=\"ico\"></i>\n" + 
				"								<span class=\"comment_wrap\">\n" + 
				"									<em id=\"floating_bottom_comment_label\">\n" + 
				"										\n" + 
				"										\n" + 
				"											댓글\n" + 
				"										\n" + 
				"									</em>\n" + 
				"									<em id=\"floating_bottom_commentCount\">\n" + 
				"										\n" + 
				"											17\n" + 
				"										\n" + 
				"									</em>\n" + 
				"								</span>\n" + 
				"								\n" + 
				"									<i class=\"icon_new\"><span class=\"new_comment\">새 댓글</span></i>\n" + 
				"								\n" + 
				"							</a>\n" + 
				"						</div>\n" + 
				"					\n" + 
				"					\n" + 
				"				</div>\n" + 
				"				<!-- [D]기타 보내기 클릭시 shared_dom show, 주소 복사시 shared_dom_copied show -->\n" + 
				"				\n" + 
				"				<a href=\"#\" onclick=\"return false;\" id=\"spiButton\" class=\"naver-splugin btn_splugin share\"\n" + 
				"					 data-style=\"unity\"\n" + 
				"					 data-url=\"https://blog.naver.com/wood-24/223264915610\"\n" + 
				"					 data-oninitialize=\"splugin_oninitialize(1);\"\n" + 
				"					 data-me-display=\"off\"\n" + 
				"					 data-likeServiceId=\"BLOG\"\n" + 
				"					 data-likeContentsId=\"wood-24_223264915610\"\n" + 
				"					 data-canonical-url=\"https://blog.naver.com/wood-24/223264915610\"\n" + 
				"				     data-logDomain=\"https://proxy.blog.naver.com/spi/v1/api/shareLog\"\n" + 
				"					 data-option=\"{baseElement:'spiButton', layerPosition:'outside-top', align:'right', top:16, left:16, marginLeft:16, marginTop:10}\">\n" + 
				"					<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"49\" height=\"49\" style=\"pointer-events: none;\"><g stroke=\"#444\" fill=\"none\" fill-rule=\"evenodd\"><path stroke-linejoin=\"round\" d=\"M32 18.5L27.025 14v3H24.42c-3.257 0-5.136 2.753-5.42 5.786 0 0 1.182-2.786 5.31-2.786h2.715v3L32 18.5z\"></path><path d=\"M28 25.517V30H14V17h4.516\"></path></g></svg>\n" + 
				"					<span class=\"blind\">공유하기</span>\n" + 
				"				</a>\n" + 
				"			</div>\n" + 
				"		</div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/mylog/floating/FloatingBarSearch-c048792_https.js\"></script>\n" + 
				"\n" + 
				"<script src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/LayerPopup-a141e19_https.js\"></script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/lib/jquery/3.2.1/jquery-3.2.1-d2d8ca4_https.js\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/lib/jquery/jquery.loadTemplate-1.5.10.min-c0311fc_https.js\"></script>\n" + 
				"<link rel=\"stylesheet\" href=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/css/blogdomain/BlogDomainRegisterLayer-717ef83_https.css\" type=\"text/css\"/>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/lottie.min-0e23629_https.js\"></script>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/BlogDomainRegisterLayer-908610778_https.js\" charset=\"UTF-8\"></script>\n" + 
				"<template id=\"blogDomainRegisterLayerTemplate\">\n" + 
				"    <div id=\"sellerAlertLayer\" class=\"SetBlogId_layer_wrap\" style=\"display:none;\">\n" + 
				"        <div class=\"caution_alert_content\">\n" + 
				"            <i class=\"icon\"></i>\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <strong class=\"title\">블로그 주소 변경 불가 안내</strong>\n" + 
				"                <p class=\"desc\">\n" + 
				"                    블로그 마켓 판매자의 이력 관리를 위해<br>\n" + 
				"                    블로그 주소 변경이 불가합니다.\n" + 
				"                </p>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <a href=\"https://help.naver.com/alias/blog/blogmarket/blogmarket14.naver\" class=\"btn\">자세히 보기</a>\n" + 
				"                <button id=\"sellerAlertLayerClose\" type=\"button\" class=\"close_btn\">레이어 닫기</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"blogDomainRegisterSuggestionLayer\" class=\"SetBlogId_layer_wrap IntroBlogId_layer\" style=\"display: none;\">\n" + 
				"        <div class=\"BlogId_content\">\n" + 
				"            <div class=\"img_area\">\n" + 
				"                <img src=\"https://ssl.pstatic.net/static/blog/img_ani_blogid1.gif\" width=\"335\" height=\"260\" class=\"img\" alt=\"블로그 아이디가 필요해요!\">\n" + 
				"            </div>\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <strong class=\"title\">블로그 아이디가 필요해요!</strong>\n" + 
				"                <p class=\"desc\">\n" + 
				"                    블로그에서 진짜 나를 기록하고<br>\n" + 
				"                    다양한 이웃과 소식을 만나보세요. 지금 시작해볼까요?\n" + 
				"                </p>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <a id=\"blogDomainRegisterStartBtn\" onclick=\"nclk_v2(this, 'bsu.popupok', '', '');return false;\" class=\"btn\">블로그 아이디 만들기</a>\n" + 
				"                <a id=\"blogDomainRegisterSuggestionLayerCloseBtn\" onclick=\"nclk_v2(this, 'bsu.popupclose', '', '');return false;\" class=\"close_btn\">블로그 아이디 만들기 레이어 닫기</a>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"domainInputLayer\" class=\"SetBlogId_layer_wrap\" style=\"display: none;\">\n" + 
				"        <div class=\"BlogId_content\">\n" + 
				"            <h1 id=\"blogDomainTitle\" class=\"title_h1\"></h1>\n" + 
				"            <div class=\"domain\">\n" + 
				"                blog.naver.com/<strong id=\"domainIdInfo\" class=\"blogid\"></strong>\n" + 
				"            </div>\n" + 
				"            <!-- 오류 .error -->\n" + 
				"            <!-- 인증 성공 .success -->\n" + 
				"            <div id=\"blogidMessageWrap\" class=\"input_blogid_area\">\n" + 
				"                <input id=\"domainInput\" class=\"input_text\" type=\"text\" placeholder=\"블로그아이디\" maxlength=\"20\" autocomplete=\"off\">\n" + 
				"                <div class=\"right_area\">\n" + 
				"                    <button id=\"clearDomainInputBtn\" type=\"button\" class=\"del_btn\" style=\"display: none\">\n" + 
				"                        입력 내용 삭제\n" + 
				"                    </button>\n" + 
				"                    <!-- 아이디 입력중 -->\n" + 
				"                    <i class=\"loading_icon\" id=\"loading_icon\"></i>\n" + 
				"                </div>\n" + 
				"                <div id=\"domainMessage\" class=\"message\"></div>\n" + 
				"            </div>\n" + 
				"            <div id= \"recommendDomainIdDiv\" class=\"recommend_blogid\">\n" + 
				"                <ul id=\"recommendDomainId\" class=\"list\">\n" + 
				"                </ul>\n" + 
				"            </div>\n" + 
				"            <div class=\"desc_area\">\n" + 
				"                <ul class=\"list\">\n" + 
				"                    <li class=\"item\">\n" + 
				"                        설정한 아이디는 나중에 변경할 수 없으니 신중하게 입력해주세요.\n" + 
				"                    </li>\n" + 
				"                    <li class=\"item blogDomainChangeOnlyViewDesc\">\n" + 
				"                        변경 전 공유된 블로그/글/모먼트 링크는 연결이 끊길 수 있습니다.\n" + 
				"                    </li>\n" + 
				"                    <li class=\"item\">\n" + 
				"                        <strong class=\"strong\">네이버 아이디 또는 개인정보가 포함된 문자 사용</strong>은 피해주세요.\n" + 
				"                    </li>\n" + 
				"                    <li class=\"item blogDomainChangeOnlyViewDesc\">\n" + 
				"                        블로그 도움말에서 아이디 변경 유의사항을 확인해보세요.\n" + 
				"                    </li>\n" + 
				"                </ul>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button id=\"blogDomainRegisterCancelBtn\" type=\"button\" class=\"cancel_btn\">\n" + 
				"                    나중에 할게요\n" + 
				"                </button>\n" + 
				"                <input id=\"domainRegisterBtn\" type=\"submit\" value=\"확인\" class=\"submit_btn\" onclick=\"return false;\" disabled=\"\">\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"domainChangeConfirmLayer\" class=\"SetBlogId_layer_wrap SetBlogIdAlert_wrap\" style=\"display:none;\">\n" + 
				"        <div class=\"SetBlogIdAlert_content\">\n" + 
				"            <div class=\"blogid_area\">\n" + 
				"                <div id=\"changeConfirmDomainId\" class=\"blogid\"></div>\n" + 
				"                <div id=\"changeConfirmDomainURL\" class=\"domain\"></div>\n" + 
				"            </div>\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <p class=\"text_small\">\n" + 
				"                    1. 이전 주소로 공유된 글은<br>\n" + 
				"                    3개월간 새로운 주소로 연결을 지원하며<br>\n" + 
				"                    이후 언제든 연결이 끊길 수 있습니다.\n" + 
				"                </p>\n" + 
				"                <p class=\"text_small\">\n" + 
				"                    2. 블로그 아이디는 한번 변경하면<br>\n" + 
				"                    <strong class=\"red_color\">다시 변경이 불가능</strong>합니다.\n" + 
				"                </p>\n" + 
				"                <p class=\"text_small\">\n" + 
				"                    <strong>변경하시겠습니까?</strong>\n" + 
				"                </p>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button id=\"changeConfirmCancleBtn\" type=\"button\" class=\"btn\">취소</button>\n" + 
				"                <button id=\"changeConfirmSubmitBtn\" type=\"button\" class=\"btn\">확인</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"domainConfirmLayer\" class=\"SetBlogId_layer_wrap SetBlogIdAlert_wrap\" style=\"display:none;\">\n" + 
				"        <div class=\"SetBlogIdAlert_content\">\n" + 
				"            <div class=\"blogid_area\">\n" + 
				"                <div class=\"img_area\">\n" + 
				"                    <img src=\"https://ssl.pstatic.net/static/blog/img_ani_blogid2.gif\" width=\"208\" height=\"86\" alt=\"섬네일\">\n" + 
				"                </div>\n" + 
				"                <div id=\"confirmDomainId\" class=\"blogid\"></div>\n" + 
				"                <div id=\"confirmDomainURL\" class=\"domain\"></div>\n" + 
				"            </div>\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <p class=\"text_red\">\n" + 
				"                    블로그 아이디는 한번 정하면\n" + 
				"                    <br>\n" + 
				"                    다시 변경이 불가능합니다.\n" + 
				"                </p>\n" + 
				"                <p class=\"text\">이 아이디로 블로그를 만들까요?</p>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button id=\"confirmCancleBtn\" type=\"button\" class=\"btn\">취소</button>\n" + 
				"                <button id=\"confirmSubmitBtn\" type=\"button\" class=\"btn\">확인</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"domainWelcomeLayer\" class=\"SetBlogId_layer_wrap WelcomeSetBlogId_layer_wrap\" style=\"display:none\">\n" + 
				"        <div class=\"BlogId_content\">\n" + 
				"            <h1 class=\"title_h1\">\n" + 
				"                환영합니다!<br>\n" + 
				"                블로그 아이디가 만들어졌어요.\n" + 
				"            </h1>\n" + 
				"            <div class=\"img_area\">\n" + 
				"                <img src=\"https://ssl.pstatic.net/static/blog/img_ani_blogid3_2.gif\" class=\"img\" width=\"500\" height=\"281\" alt=\"섬네일\"/>\n" + 
				"            </div>\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <div id=\"welcomeBlogId\" class=\"blogid\"></div>\n" + 
				"                <div id=\"welcomeBlogURL\" class=\"domain\">\n" + 
				"                    blog.naver.com/\n" + 
				"                </div>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <a id=\"closeWelcomeLayer\" onclick=\"nclk_v2(this, 'bsu*i.start', '', '');return false;\" class=\"btn blue\">바로 시작하기</a>\n" + 
				"                <a id=\"nextStep\" onclick=\"nclk_v2(this, 'bsu*i.inform', '', '');return false;\" class=\"btn green\">추가정보 입력하기</a>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"blogBasicInfoLayer\" class=\"SetBlogId_layer_wrap InputBlogId_layer_wrap\" style=\"display:none\">\n" + 
				"        <div class=\"BlogId_content\">\n" + 
				"            <h1 class=\"title_h1\">기본정보를 입력해주세요.</h1>\n" + 
				"            <p class=\"title_desc\">나중에 언제든지 변경할 수 있어요.</p>\n" + 
				"            <div class=\"profile_area\">\n" + 
				"                <div class=\"profile\">\n" + 
				"                    <div id=\"blogDomainProfileImage\" class=\"img\" title=\"\" style=\"background-image: url(https://ssl.pstatic.net/static/blog/profile/Profile_defaultIMG_A@2x.png);\"></div>\n" + 
				"                </div>\n" + 
				"                <button id=\"profileSetBtn\" onclick=\"nclk_v2(this, 'bsu*p.profile', '', '');return false;\"  type=\"button\" class=\"profile_set_btn\">프로필 이미지 설정</button>\n" + 
				"                <div id=\"profileSetLayer\" class=\"profile_set_layer\">\n" + 
				"                    <div class=\"top_btn_area\">\n" + 
				"                        <button id=\"profileImageClear\" onclick=\"nclk_v2(this, 'bsu*p.prfdelete', '', '');\" type=\"button\" class=\"del_btn\">지우기</button>\n" + 
				"                        <div class=\"file_btn\">\n" + 
				"                            <label class=\"label\" for=\"profile_upload_btn\">사진 업로드</label>\n" + 
				"                            <input type=\"file\" onclick=\"nclk_v2(this, 'bsu*p.prfupload', '', '');\" id=\"profile_upload_btn\" class=\"input_file\" accept=\".png, .jpg, .jpeg, .gif, .bmp\"/>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <ul id=\"profilePresetList\" class=\"profile_list\">\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop1', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_01.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋1\"  /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop2', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_02.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋2\"  /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop3', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_03.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋3\"  /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop4', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_04.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋4\"  /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop5', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_05.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋5\"  /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop6', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_06.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋6\"  /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop7', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_07.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋7\" /></button>\n" + 
				"                        </li>\n" + 
				"                        <li class=\"item\">\n" + 
				"                            <button type=\"button\" onclick=\"nclk_v2(this, 'bsu*p.prfop8', '', '');\" class=\"btn\"><img src=\"https://ssl.pstatic.net/static/blog/profile/img_profile_preset_08.png\" width=\"59\" height=\"59\" class=\"img\" alt=\"프리셋8\" /></button>\n" + 
				"                        </li>\n" + 
				"                    </ul>\n" + 
				"                    <div class=\"btn_area\">\n" + 
				"                        <button id=\"proflieImageCancelBtn\" onclick=\"nclk_v2(this, 'bsu*p.prfccl', '', '');\" type=\"button\" class=\"cancel_btn\">취소</button>\n" + 
				"                        <button id=\"proflieImageSelectedBtn\" onclick=\"nclk_v2(this, 'bsu*p.prfok', '', '');\" type=\"button\" class=\"submit_btn\">적용</button>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"            </div>\n" + 
				"            <div class=\"input_area\">\n" + 
				"                <div class=\"input\">\n" + 
				"                    <label class=\"label\" for=\"nickname\">별명</label>\n" + 
				"                    <input onclick=\"nclk_v2(this, 'bsu*p.nickname', '', '');\" type=\"text\" class=\"input_text\" id=\"nickname\" maxlength=\"20\" autocomplete=\"off\"/>\n" + 
				"                </div>\n" + 
				"                <div class=\"input\">\n" + 
				"                    <label class=\"label\" for=\"blogname\">블로그명</label>\n" + 
				"                    <input onclick=\"nclk_v2(this, 'bsu*p.blogname', '', '');\" type=\"text\" class=\"input_text\" id=\"blogname\" maxlength=\"50\" autocomplete=\"off\"/>\n" + 
				"                </div>\n" + 
				"                <div class=\"input\">\n" + 
				"                    <label class=\"label\">블로그 주제</label>\n" + 
				"                    <button id=\"showBlogThemeList\" onclick=\"nclk_v2(this, 'bsu*p.blogtopic', '', '');\" type=\"button\" class=\"theme_btn\">주제 없음</button>\n" + 
				"                    <div id=\"basicInfoBlogThemeList\" class=\"theme_list_layer\">\n" + 
				"                        <strong class=\"title\">엔터테인먼트·예술</strong>\n" + 
				"                        <ul class=\"list\">\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option1', '', '');\" class=\"input_radio\" name=\"theme\"  id=\"theme1\" data-directoryseq=\"5\">\n" + 
				"                                <label for=\"theme1\" class=\"label\">문학·책</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option2', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme2\" data-directoryseq=\"6\">\n" + 
				"                                <label for=\"theme2\" class=\"label\">영화</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option3', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme3\" data-directoryseq=\"8\">\n" + 
				"                                <label for=\"theme3\" class=\"label\">미술·디자인</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option4', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme4\" data-directoryseq=\"7\">\n" + 
				"                                <label for=\"theme4\" class=\"label\">공연·전시</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option5', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme5\" data-directoryseq=\"11\">\n" + 
				"                                <label for=\"theme5\" class=\"label\">음악</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option6', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme6\" data-directoryseq=\"9\">\n" + 
				"                                <label for=\"theme6\" class=\"label\">드라마</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option7', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme7\" data-directoryseq=\"12\">\n" + 
				"                                <label for=\"theme7\" class=\"label\">스타·연예인</label>\n" + 
				"                            </li>\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option8', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme8\" data-directoryseq=\"13\">\n" + 
				"                                <label for=\"theme8\" class=\"label\">만화·애니</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option9', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme9\" data-directoryseq=\"10\">\n" + 
				"                                <label for=\"theme9\" class=\"label\">방송</label>\n" + 
				"                            </li>\n" + 
				"                        </ul>\n" + 
				"                        <strong class=\"title\">생활·노하우·쇼핑</strong>\n" + 
				"                        <ul class=\"list\">\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option10', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme10\" data-directoryseq=\"14\">\n" + 
				"                                <label for=\"theme10\" class=\"label\">일상·생각</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option11', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme11\" data-directoryseq=\"15\">\n" + 
				"                                <label for=\"theme11\" class=\"label\">육아·결혼</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option12', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme12\" data-directoryseq=\"16\">\n" + 
				"                                <label for=\"theme12\" class=\"label\">반려동물</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option13', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme13\" data-directoryseq=\"17\">\n" + 
				"                                <label for=\"theme13\" class=\"label\">좋은글·이미지</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option14', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme14\" data-directoryseq=\"18\">\n" + 
				"                                <label for=\"theme14\" class=\"label\">패션·미용</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option15', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme15\" data-directoryseq=\"19\">\n" + 
				"                                <label for=\"theme15\" class=\"label\">인테리어·DIY</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option16', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme16\" data-directoryseq=\"20\">\n" + 
				"                                <label for=\"theme16\" class=\"label\">요리·레시피</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option17', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme17\" data-directoryseq=\"21\">\n" + 
				"                                <label for=\"theme17\" class=\"label\">상품리뷰</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option18', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme18\" data-directoryseq=\"36\">\n" + 
				"                                <label for=\"theme18\" class=\"label\">원예·재배</label>\n" + 
				"                            </li>\n" + 
				"                        </ul>\n" + 
				"                        <strong class=\"title\">취미·여가·여행</strong>\n" + 
				"                        <ul class=\"list\">\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option19', '', '');\" onclick=\"nclk_v2(this, 'bsu*p.option1', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme19\" data-directoryseq=\"22\">\n" + 
				"                                <label for=\"theme19\" class=\"label\">게임</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option20', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme20\" data-directoryseq=\"23\">\n" + 
				"                                <label for=\"theme20\" class=\"label\">스포츠</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option21', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme21\" data-directoryseq=\"24\">\n" + 
				"                                <label for=\"theme21\" class=\"label\">사진</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option22', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme22\" data-directoryseq=\"25\">\n" + 
				"                                <label for=\"theme22\" class=\"label\">자동차</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option23', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme23\" data-directoryseq=\"26\">\n" + 
				"                                <label for=\"theme23\" class=\"label\">취미</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option24', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme24\" data-directoryseq=\"27\">\n" + 
				"                                <label for=\"theme24\" class=\"label\">국내여행</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option25', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme25\" data-directoryseq=\"28\">\n" + 
				"                                <label for=\"theme25\" class=\"label\">세계여행</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option26', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme26\" data-directoryseq=\"29\">\n" + 
				"                                <label for=\"theme26\" class=\"label\">맛집</label>\n" + 
				"                            </li>\n" + 
				"                        </ul>\n" + 
				"                        <strong class=\"title\">지식·동향</strong>\n" + 
				"                        <ul class=\"list\">\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option27', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme27\" data-directoryseq=\"30\">\n" + 
				"                                <label for=\"theme27\" class=\"label\">IT·컴퓨터</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option28', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme28\" data-directoryseq=\"31\">\n" + 
				"                                <label for=\"theme28\" class=\"label\">사회·정치</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option29', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme29\" data-directoryseq=\"32\">\n" + 
				"                                <label for=\"theme29\" class=\"label\">건강·의학</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option30', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme30\" data-directoryseq=\"33\">\n" + 
				"                                <label for=\"theme30\" class=\"label\">비즈니스·경제</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option31', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme31\" data-directoryseq=\"35\">\n" + 
				"                                <label for=\"theme31\" class=\"label\">어학·외국어</label>\n" + 
				"                            </li>\n" + 
				"\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option32', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme32\" data-directoryseq=\"34\">\n" + 
				"                                <label for=\"theme32\" class=\"label\">교육·학문</label>\n" + 
				"                            </li>\n" + 
				"                        </ul>\n" + 
				"                        <strong class=\"title\">주제없음</strong>\n" + 
				"                        <ul class=\"list\">\n" + 
				"                            <li class=\"item\">\n" + 
				"                                <input type=\"radio\" onclick=\"nclk_v2(this, 'bsu*p.option33', '', '');\" class=\"input_radio\" name=\"theme\" id=\"theme33\" data-directoryseq=\"0\">\n" + 
				"                                <label for=\"theme33\" class=\"label\">주제 선택 보류</label>\n" + 
				"                            </li>\n" + 
				"                        </ul>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button id=\"closeBlogBasicInfoLayer\" onclick=\"nclk_v2(this, 'bsu*p.ccl', '', '', event);\"  type=\"button\" class=\"cancel_btn\">\n" + 
				"                    나중에 할게요\n" + 
				"                </button>\n" + 
				"                <input id=\"submitBlogBasicInfo\" onClick=\"nclk_v2(this, 'bsu*p.next', '', ''); return false;\" type=\"submit\" value=\"다음\" class=\"submit_btn\" />\n" + 
				"            </div>\n" + 
				"            <div class=\"pagination_area\">\n" + 
				"                <i class=\"item is_active\">기본정보 입력</i>\n" + 
				"                <i class=\"item\">관심분야 선택</i>\n" + 
				"                <i class=\"item\">이웃 맺기</i>\n" + 
				"            </div>\n" + 
				"            <div class=\"back_btn_area\">\n" + 
				"                <button id=\"blogBasicInfoBackBtn\" onclick=\"nclk_v2(this, 'bsu*p.previous', '', '');\" type=\"button\" class=\"back_btn\">뒤로가기 버튼</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"personalThemeLayer\" class=\"SetBlogId_layer_wrap InputBlogId_layer_wrap\" style=\"display:none\">\n" + 
				"        <div class=\"BlogId_content\">\n" + 
				"            <h1 class=\"title_h1\">관심분야를 선택해주세요.</h1>\n" + 
				"            <p class=\"title_desc\">선택한 분야의 글과 이웃을 추천받을 수 있어요.</p>\n" + 
				"            <div class=\"recommendation_list\">\n" + 
				"                <ul id=\"personalThemeList\" class=\"list\">\n" + 
				"\n" + 
				"                </ul>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button id=\"personalThemeSelectCancel\" onClick=\"nclk_v2(this, 'bsu*t.ccl', '', '');\" type=\"button\" class=\"cancel_btn\">\n" + 
				"                    나중에 할게요\n" + 
				"                </button>\n" + 
				"                <!-- 사용할 수 있는 아이디 입력시 disabled 삭제 -->\n" + 
				"                <input id=\"personalThemeSubmitBtn\" onclick=\"nclk_v2(this, 'bsu*t.next', '', ''); return false;\" type=\"submit\" value=\"다음\" class=\"submit_btn\" />\n" + 
				"            </div>\n" + 
				"            <div class=\"pagination_area\">\n" + 
				"                <i class=\"item\">기본정보 입력</i>\n" + 
				"                <i class=\"item is_active\">관심분야 선택</i>\n" + 
				"                <i class=\"item\">이웃 맺기</i>\n" + 
				"            </div>\n" + 
				"            <div class=\"back_btn_area\">\n" + 
				"                <button id=\"personalThemeBackBtn\" onclick=\"nclk_v2(this, 'bsu*t.previous', '', '');\" type=\"button\" class=\"back_btn\">뒤로가기 버튼</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div id=\"recommendBlogLayer\" class=\"SetBlogId_layer_wrap InputBlogId_layer_wrap\" style=\"display:none;\">\n" + 
				"        <div class=\"BlogId_content\">\n" + 
				"            <h1 class=\"title_h1\">인기블로거와 이웃을 맺으세요.</h1>\n" + 
				"            <p class=\"title_desc\">이웃을 맺으면 이웃새글에서 글을 받아볼 수 있어요.</p>\n" + 
				"            <div class=\"bloger_list\">\n" + 
				"                <ul id=\"recommendBlogList\" class=\"list\">\n" + 
				"\n" + 
				"                </ul>\n" + 
				"                <div class=\"loading_wrap\" id=\"recommendBlogList_loading_wrap\">\n" + 
				"                    <div class=\"colordot_loading\">\n" + 
				"                        <i class=\"dot dot1\"></i>\n" + 
				"                        <i class=\"dot dot2\"></i>\n" + 
				"                        <i class=\"dot dot3\"></i>\n" + 
				"                        <i class=\"dot dot4\"></i>\n" + 
				"                        <i class=\"dot dot5\"></i>\n" + 
				"                        <i class=\"dot dot6\"></i>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button id=\"startBlogBtn\" onclick=\"nclk_v2(this, 'bsu*r.start', '', '');\" type=\"button\" class=\"start_blog_btn\">\n" + 
				"                    블로그 시작하기\n" + 
				"                </button>\n" + 
				"            </div>\n" + 
				"            <div class=\"pagination_area\">\n" + 
				"                <i class=\"item\">기본정보 입력</i>\n" + 
				"                <i class=\"item\">관심분야 선택</i>\n" + 
				"                <i class=\"item is_active\">이웃 맺기</i>\n" + 
				"            </div>\n" + 
				"            <div class=\"back_btn_area\">\n" + 
				"                <button id=\"recommendBackBtn\" onclick=\"nclk_v2(this, 'bsu*r.previous', '', '');\" type=\"button\" class=\"back_btn\">뒤로가기 버튼</button>\n" + 
				"            </div>\n" + 
				"            </fieldset>\n" + 
				"            </form>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"\n" + 
				"    <div class=\"SetBlogId_layer_wrap\" style=\"display:none;\" id=\"alertLayer\">\n" + 
				"        <div class=\"ui_alert_content\">\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <p class=\"desc\" id=\"alertMessage\"></p>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button type=\"button\" class=\"btn\" id=\"closeAlertLayer\">확인</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"\n" + 
				"    <div class=\"SetBlogId_layer_wrap\" style=\"display:none;\" id=\"alertLayeCallBack\">\n" + 
				"        <div class=\"ui_alert_content\">\n" + 
				"            <div class=\"text_area\">\n" + 
				"                <p class=\"desc\" id=\"alertMessageCallBack\"></p>\n" + 
				"            </div>\n" + 
				"            <div class=\"btn_area\">\n" + 
				"                <button type=\"button\" class=\"btn\" id=\"closeAlertLayerCallBack\">확인</button>\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"</template>\n" + 
				"\n" + 
				"<template id=\"blogDomainRegisterPopularBlogTemplate\" style=\"display: none\">\n" + 
				"    <li class=\"item\">\n" + 
				"        <div class=\"bloger_item\">\n" + 
				"            <div class=\"bloger\">\n" + 
				"                <div class=\"profile_img\">\n" + 
				"                    <img data-src=\"profileImage\" width=\"56\" height=\"56\" class=\"img\" alt=\"프로필\" />\n" + 
				"                </div>\n" + 
				"                <div class=\"profile_text\">\n" + 
				"                    <strong data-content=\"nickName\" class=\"nickname\"></strong>\n" + 
				"                    <span data-content=\"blogName\" class=\"blogname\"></span>\n" + 
				"                    <span data-content=\"themeName\" class=\"category\"></span>\n" + 
				"                </div>\n" + 
				"            </div>\n" + 
				"            <div class=\"post_list_wrap\">\n" + 
				"                <ul data-innerHTML=\"postList\" class=\"list\">\n" + 
				"                </ul>\n" + 
				"            </div>\n" + 
				"            <button type=\"button\" class=\"add_buddy_btn _recommendBuddyAdd\" data-template-bind='[{\"attribute\": \"style\", \"value\": \"buddyAddDisplayStyle\"}, {\"attribute\": \"data-domainIdOrBlogId\", \"value\": \"domainIdOrBlogId\"}]'>이웃추가</button>\n" + 
				"            <button class=\"buddy _recommendBuddyRemove\" data-template-bind='[{\"attribute\": \"style\", \"value\": \"buddyDisplayStyle\"}, {\"attribute\": \"data-domainIdOrBlogId\", \"value\": \"domainIdOrBlogId\"}]'>이웃</button>\n" + 
				"        </div>\n" + 
				"    </li>\n" + 
				"</template>\n" + 
				"\n" + 
				"<template id=\"blogDomainRegisterPopularBlogPostTemplate\" style=\"display: none\">\n" + 
				"    <li class=\"item\">\n" + 
				"        <div class=\"img_area\">\n" + 
				"            <img data-src=\"mainThumbnailUrl\" class=\"img\" alt=\"섬네일\" />\n" + 
				"        </div>\n" + 
				"        <div class=\"text_area\">\n" + 
				"            <strong data-content=\"title\" class=\"title\"></strong>\n" + 
				"            <p class=\"desc\" data-content=\"briefContents\"></p>\n" + 
				"        </div>\n" + 
				"    </li>\n" + 
				"</template>\n" + 
				"\n" + 
				"<template id=\"personalThemeTemplate\" style=\"display: none\">\n" + 
				"    <li class=\"item\">\n" + 
				"        <div class=\"recommend_item\">\n" + 
				"            <input type=\"checkbox\" class=\"inputbox\" data-template-bind='[{\"attribute\": \"id\", \"value\": \"id\"}]'>\n" + 
				"            <label data-template-bind='[\n" + 
				"                {\"attribute\": \"for\", \"value\": \"id\"},\n" + 
				"                {\"attribute\": \"data-directorySeq\", \"value\": \"directorySeq\"},\n" + 
				"                {\"attribute\": \"data-clickCode\", \"value\": \"clickCode\"}]' data-content=\"themeName\" class=\"label\"></label>\n" + 
				"        </div>\n" + 
				"    </li>\n" + 
				"</template>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/BeyondAnalytics-533008747_https.js\" charset=\"UTF-8\"></script>\n" + 
				"\n" + 
				"<script>\n" + 
				"    function baPostOnlyViewEvent(blogNo, logNo) {\n" + 
				"        var baObject = new Object();\n" + 
				"        baObject.naverId = userNaverId;\n" + 
				"        baObject.scene_id = \"post_end\";\n" + 
				"        baObject.classifier = \"post_end\";\n" + 
				"        baObject.baUrl = 'https://scv-blog.io.naver.com/jackpotlog/v1/logs';\n" + 
				"        baObject.action_id = \"scene_enter\";\n" + 
				"\n" + 
				"        var extra = new Object();\n" + 
				"        extra.blog_no = blogNo;\n" + 
				"        extra.post_id = logNo;\n" + 
				"\n" + 
				"        if (!!parent) {\n" + 
				"            /*\n" + 
				"             * ê°ì¸ ë¸ë¡ê·¸ ë´ë¶ ì´ëì(blog.naver.com)\n" + 
				"             * ê²ì ì§ìì parent.document.URLê³¼ document.referrerì ê°ì ê°ë¤\n" + 
				"             */\n" + 
				"            if (parent.document.URL !== document.referrer) { //ë¸ë¡ëë´ ì´ëì\n" + 
				"                extra.referer = parent.document.URL;\n" + 
				"            } else if (parent.document.referrer !== \"\") { //ì¸ë¶ ì§ìì\n" + 
				"                extra.referer = parent.document.referrer;\n" + 
				"                if (parent.document.referrer.includes(\"search.naver.com\")) {\n" + 
				"                    extra.tracking_code = \"naver_search\";\n" + 
				"                }\n" + 
				"            }\n" + 
				"        } else if (document.referrer.includes(\"search.naver.com\")) {\n" + 
				"            extra.tracking_code = \"naver_search\";\n" + 
				"        }\n" + 
				"\n" + 
				"        extra.b_relationtype = blogOwnerRelationType;\n" + 
				"        extra.u_relationtype = relationType;\n" + 
				"        baObject.extra = extra;\n" + 
				"\n" + 
				"        baMylog.init(baObject);\n" + 
				"        baMylog.send();\n" + 
				"    }\n" + 
				"\n" + 
				"    function baCommentEvent(logNo) {\n" + 
				"        // postlistì¼ë ì ì¡íì§ ìì\n" + 
				"        if (logNo === '') {\n" + 
				"            return;\n" + 
				"        }\n" + 
				"\n" + 
				"        var baObject = new Object();\n" + 
				"        baObject.naverId = userNaverId;\n" + 
				"        baObject.scene_id = \"post_end\";\n" + 
				"        baObject.classifier = \"comment_write_button\";\n" + 
				"        baObject.action_id = \"click\";\n" + 
				"        baObject.baUrl = 'https://scv-blog.io.naver.com/jackpotlog/v1/logs';\n" + 
				"\n" + 
				"        var extra = new Object();\n" + 
				"        extra.blog_no = blogNo;\n" + 
				"        extra.post_id = logNo;\n" + 
				"        baObject.extra = extra;\n" + 
				"\n" + 
				"        baMylog.init(baObject);\n" + 
				"        baMylog.send();\n" + 
				"    }\n" + 
				"</script>\n" + 
				"<div id=\"domainRegisterWrap\"></div>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"    jQuery.noConflict();\n" + 
				"</script>\n" + 
				"	<div id=\"head-skin\">\n" + 
				"		<div id=\"body\">\n" + 
				"		  	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<a href=\"#blog-menu\" onclick=\"var t=document.getElementById('blog-menu');t.tabIndex=-1;t.focus();return false;\" class=\"skip\">메뉴 바로가기</a>\n" + 
				"<a href=\"#post_1\" id=\"goContentsAreaLink\" class=\"skip\">본문 바로가기</a>\n" + 
				"<div id=\"blog-gnb\">\n" + 
				"<div id=\"gnb-area\" class=\"gnb\">\n" + 
				"	<ul>\n" + 
				"	\n" + 
				"		<li class=\"i3\">\n" + 
				"			\n" + 
				"				\n" + 
				"					<a href=\"https://blog.naver.com/MyBlog.naver\" onclick=\"nclk_v2(this,'gnb_oth.my','','');\" class=\"link\" target=\"_top\">내 블로그</a> <img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"				\n" + 
				"				\n" + 
				"			\n" + 
				"		</li>\n" + 
				"	\n" + 
				"		<li class=\"i4\">\n" + 
				"		\n" + 
				"			<a href=\"https://nid.naver.com/nidlogin.login?mode=form&url=https://blog.naver.com/wood-24\" onclick=\"nclk_v2(this,'gnb_oth.other','','');\" class=\"link\" target=\"_top\">이웃블로그</a>\n" + 
				"		\n" + 
				"		\n" + 
				"		\n" + 
				"		<img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"		<div id=\"buddydiv\" style=\"display:none\">\n" + 
				"		<div class=\"buddylist_bg\"></div>\n" + 
				"		<span class=\"ico_arrow\"></span>\n" + 
				"		<div id=\"buddylist\"></div>\n" + 
				"		</div>\n" + 
				"		</li>\n" + 
				"		<li class=\"i2\"><a href=\"https://section.blog.naver.com\" onclick=\"nclk_v2(this,'gnb_oth.all','','')\" class=\"link\" target=\"_top\">블로그 홈</a> <img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" /></li>\n" + 
				"		\n" + 
				"		\n" + 
				"		<li class=\"i1 login\">\n" + 
				"			<a href=\"https://nid.naver.com/nidlogin.login?mode=form&url=https://blog.naver.com/wood-24/223264915610\" class=\"btn_blog_login\" onclick=\"nclk_v2(this,'gnb_oth.login','','');\" target=\"_top\"><span class=\"blind\">로그인</span></a>\n" + 
				"		</li>\n" + 
				"		\n" + 
				"		<li class=\"i1 blog_gnb_wrap\">\n" + 
				"			<div id=\"gnb\"></div>\n" + 
				"		</li>\n" + 
				"	</ul>\n" + 
				"</div>\n" + 
				"</div>\n" + 
				"<!-- blog_gnb -->\n" + 
				"\n" + 
				"<!-- <div id=\"highlight_turn_on\" class=\"highlight_off _hightlightOpen\" style=\"display:none;\">\n" + 
				"	<button type=\"button\" class=\"btn_op _hightlightOpen\">검색어 켜기</button>\n" + 
				"</div> -->\n" + 
				"\n" + 
				"<hr />\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"	var gnb_service=\"blog\";\n" + 
				"	var gnb_template=\"gnb_utf8\";\n" + 
				"	var gnb_brightness=1;\n" + 
				"	var gnb_url='https://blog.naver.com/wood-24/223264915610';\n" + 
				"	var gnb_logout = gnb_url;\n" + 
				"	var gnb_login = gnb_url;\n" + 
				"	var gnb_login_on_top = true;\n" + 
				"	var gnb_item_hide_option= 1|4|8|16|128;\n" + 
				"	var isGetGnbCalled = false;\n" + 
				"	\n" + 
				"	window.onload = function() {\n" + 
				"		if (!isGetGnbCalled) {\n" + 
				"			getGNB();\n" + 
				"			isGetGnbCalled = true;\n" + 
				"		}\n" + 
				"		setSkipNavigationInfo();\n" + 
				"	};\n" + 
				"	\n" + 
				"	//웹접근성 스킵네비게이션 설정\n" + 
				"	function setSkipNavigationInfo() {\n" + 
				"		var contentsId;\n" + 
				"		if ($(\"postListBody\")) {\n" + 
				"			contentsId = \"postListBody\";\n" + 
				"		} else {\n" + 
				"			contentsId = \"post-area\";\n" + 
				"		}\n" + 
				"\n" + 
				"		if (contentsId) {\n" + 
				"			$(\"goContentsAreaLink\").href = \"#\" + contentsId;\n" + 
				"\n" + 
				"			if (document.getElementById(\"goContentsAreaLink\").addEventListener) {\n" + 
				"				document.getElementById(\"goContentsAreaLink\").addEventListener(\n" + 
				"						'click', onClickChangeFocus, false);\n" + 
				"			} else if (document.getElementById(\"goContentsAreaLink\").attachEvent) {	//IE8을 위한 이벤트 등록 리스너\n" + 
				"				document.getElementById(\"goContentsAreaLink\").attachEvent(\n" + 
				"						'click', onClickChangeFocus);\n" + 
				"			}\n" + 
				"\n" + 
				"			function onClickChangeFocus() {\n" + 
				"				var t = document.getElementById(contentsId);\n" + 
				"				t.tabIndex = -1;\n" + 
				"				t.focus();\n" + 
				"				return false;\n" + 
				"			};\n" + 
				"		}\n" + 
				"	}\n" + 
				"\n" + 
				"\n" + 
				"	var goMarketAdmin = function (isWheelEvent) {\n" + 
				"		nclk_v2(this,'gnb_myb.itemlist','','',isWheelEvent);\n" + 
				"		if (userAgentUtil.isSafariBrowser() || userAgentUtil.isUnderIE11VersionBrowser()){\n" + 
				"			if (confirm(\"이 브라우저는 현재 블로그 마켓 관리를 지원하지 않습니다.\\n 네이버 웨일 등 다른 브라우저를 이용하시기 바랍니다.\\n 지금 네이버 웨일을 설치하시겠습니까?\")) {\n" + 
				"				window.open(\"https://whale.naver.com/ko/download\");\n" + 
				"				return;\n" + 
				"			}\n" + 
				"\n" + 
				"			return;\n" + 
				"		}\n" + 
				"\n" + 
				"		if (\"\" === 'fee_not_set') {\n" + 
				"			if (confirm(\"배송비 설정을 마친 후 마켓 관리 메뉴에 접근할 수 있습니다. 배송비 설정으로 이동하시겠습니까?\")) {\n" + 
				"				window.location = 'https://seller.blog.naver.com' + \"/market/deliveryFee\";\n" + 
				"				return;\n" + 
				"			}\n" + 
				"\n" + 
				"			return;\n" + 
				"		}\n" + 
				"\n" + 
				"\n" + 
				"		if(isWheelEvent){\n" + 
				"			window.open('https://seller.blog.naver.com' + \"/market/my\");\n" + 
				"			return;\n" + 
				"		}\n" + 
				"\n" + 
				"		if (window.parent) {\n" + 
				"			window.parent.location = 'https://seller.blog.naver.com' + \"/market/my\";\n" + 
				"			return;\n" + 
				"		}\n" + 
				"\n" + 
				"		window.location = 'https://seller.blog.naver.com' + \"/market/my\";\n" + 
				"	};\n" + 
				"</script>\n" + 
				"\n" + 
				"<script id=\"gnb-script\" type=\"text/javascript\" src=\"https://ssl.pstatic.net/static.gn/templates/gnb_utf8.nhn?20231116\" charset=\"utf-8\"></script>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"    var doc = document.documentElement;\n" + 
				"    doc.setAttribute('data-useragent',navigator.userAgent);\n" + 
				"setTimeout(function() {\n" + 
				"	if (!isGetGnbCalled) {\n" + 
				"		getGNB();\n" + 
				"		isGetGnbCalled = true;\n" + 
				"	}\n" + 
				"}, 1500);\n" + 
				"</script><hr />\n" + 
				"			<div id=\"whole-border\">\n" + 
				"				<div id=\"whole-head\"></div>\n" + 
				"				<div id=\"whole-body\">\n" + 
				"					\n" + 
				"					<div id=\"top-area\">\n" + 
				"					  	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"blog-title\">\n" + 
				"<table role=\"presentation\"><tr><td id=\"blogTitleText\"><h1><a id=\"blog-title-anchor\" onclick=\"nclk_v2(this,'tit.title','','');\" href=\"https://blog.naver.com/PostList.naver?blogId=wood-24&categoryNo=12\"><span id=\"blogTitleName\" class=\"itemtitlefont\">우드24</span></a></h1></td></tr></table>\n" + 
				"</div><hr />\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"blog-menu\">\n" + 
				"	<div class=\"border\">\n" + 
				"			<table>\n" + 
				"				<caption>\n" + 
				"					<span class=\"blind\">블로그 메뉴</span>\n" + 
				"				</caption>\n" + 
				"				<tr><td nowrap=\"nowrap\" class=\"menu1\">\n" + 
				"			<ul>\n" + 
				"			\n" + 
				"			\n" + 
				"			\n" + 
				"				<li>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"				\n" + 
				"				<a href=\"https://blog.naver.com/PostList.naver?blogId=wood-24&categoryNo=12&skinType=&skinId=&from=menu\" class=\"on itemfont _doNclick _param(false|blog|)\">블로그</a>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/imgs/ico_n.gif\" alt=\"new\">\n" + 
				"				</li>\n" + 
				"			\n" + 
				"			\n" + 
				"			\n" + 
				"				<li>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"				\n" + 
				"					\n" + 
				"				\n" + 
				"				<a href=\"https://blog.naver.com/PostList.naver?blogId=wood-24&categoryNo=6&skinType=&skinId=&from=menu\" class=\"off itemfont _doNclick _param(false|category|6|1)\">제품소개</a>\n" + 
				"				\n" + 
				"				</li>\n" + 
				"			\n" + 
				"			\n" + 
				"			\n" + 
				"				<li>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"				\n" + 
				"					\n" + 
				"				\n" + 
				"				<a href=\"https://blog.naver.com/PostList.naver?blogId=wood-24&categoryNo=12&skinType=&skinId=&from=menu\" class=\"off itemfont _doNclick _param(false|category|12|2)\">시공 포트폴리오</a>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/imgs/ico_n.gif\" alt=\"new\">\n" + 
				"				</li>\n" + 
				"			\n" + 
				"			\n" + 
				"			\n" + 
				"				<li>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"				\n" + 
				"					\n" + 
				"				\n" + 
				"				<a href=\"https://blog.naver.com/PostList.naver?blogId=wood-24&categoryNo=13&skinType=&skinId=&from=menu\" class=\"off itemfont _doNclick _param(false|category|13|3)\">도움되는 시공정보</a>\n" + 
				"				\n" + 
				"				</li>\n" + 
				"			\n" + 
				"			\n" + 
				"			</ul></td><td nowrap=\"nowrap\" class=\"menu2\">\n" + 
				"			<ul>\n" + 
				"			\n" + 
				"			\n" + 
				"				<li>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"11\" class=\"bar\" alt=\"\" />\n" + 
				"				<a href=\"https://blog.naver.com/guestbook/GuestBookList.naver?blogId=wood-24&skinType=&skinId=&from=menu\"  class=\"off itemfont _doNclick _param(false|guestbook)\">안부</a>\n" + 
				"				<img src=\"https://blogimgs.pstatic.net/imgs/ico_n.gif\" alt=\"new\">\n" + 
				"				</li>\n" + 
				"			\n" + 
				"\n" + 
				"			\n" + 
				"\n" + 
				"			</ul>\n" + 
				"		</td></tr></table>\n" + 
				"	</div>\n" + 
				"</div><hr />\n" + 
				"					</div>\n" + 
				"					\n" + 
				"					<div id=\"wrapper\" class=\"clearfix\">\n" + 
				"						<div id=\"twocols\" class=\"clearfix\">\n" + 
				"							\n" + 
				"							<div id=\"content-area\" class=\"\">\n" + 
				"								\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"post-area\" class=\"post-area\">\n" + 
				"\n" + 
				"<h2 id=\"selectedMenu\" class=\"blind\">블로그</h2>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div  class=\"_naver_comment_skin_property\" style=\"position:absolute;z-index:-100;visibility:hidden\">\n" + 
				"	<span class=\"pcol2\"></span>\n" + 
				"	<span class=\"cline\"></span>\n" + 
				"	<span class=\"pcol3\"></span>\n" + 
				"	<span class=\"comment\"></span>\n" + 
				"	<span class=\"btn\"></span>\n" + 
				"	<span class=\"pcol2b\"></span>\n" + 
				"</div>\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//css/navercomment/naver_comment_blog_base-61fa3cc_https.css\" />\n" + 
				"<script type=\"text/template\" id=\"naverCommentCustomCssTpl\">\n" + 
				"#post-area .commentbox_header,\n" + 
				"#post-area .u_cbox \\{background:{=comment} !important;\\}\n" + 
				"\n" + 
				"#post-area .u_cbox .u_cbox_name_area,\n" + 
				"#post-area .u_cbox .u_cbox_target_name,\n" + 
				"#post-area .u_cbox .u_cbox_comment .u_cbox_text_wrap,\n" + 
				"#post-area .u_cbox .u_cbox_date,\n" + 
				"#post-area .u_cbox .u_cbox_btn_report,\n" + 
				"#post-area .u_cbox .u_cbox_btn_unhide,\n" + 
				"#post-area .u_cbox .u_cbox_tool .u_cbox_btn_reply,\n" + 
				"#post-area .u_cbox .u_cbox_paginate .u_cbox_cnt_page,\n" + 
				"#post-area .u_cbox .u_cbox_paginate .u_cbox_num_page,\n" + 
				"#post-area .commentbox_header .btn_write_comment,\n" + 
				"#post-area .commentbox_header .commentbox_pagination .num,\n" + 
				"#post-area .commentbox_header .btn_pagination .icon,\n" + 
				"#post-area .post-btn .postre .u_likeit_list_module .u_likeit_list_btn,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_ico_recomm:before,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_cnt_recomm,\n" + 
				"#post-area .u_cbox .u_cbox_ico_stat_secret,\n" + 
				"#post-area .u_cbox .u_cbox_work_sub .u_cbox_ico_open \\{color:{=pcol2} !important;\\}\n" + 
				"\n" + 
				"#post-area .u_cbox .u_cbox_info_base .u_cbox_ico_bar \\{background-color:{=pcol2} !important;\\}\n" + 
				"\n" + 
				"#post-area .u_cbox .u_cbox_tool .u_cbox_btn_reply:before,\n" + 
				"#post-area .u_cbox .u_cbox_work_sub .u_cbox_work_box:before,\n" + 
				"#post-area .u_cbox .u_cbox_comment_box:before,\n" + 
				"#post-area .u_cbox .u_cbox_ico_reply,\n" + 
				"#post-area .u_cbox .u_cbox_paginate strong.u_cbox_page:before,\n" + 
				"#post-area .commentbox_header:before,\n" + 
				"#post-area .commentbox_header .btn_pagination a:before,\n" + 
				"#post-area .post-btn .postre .u_likeit_list_module .u_likeit_list_btn:before,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_btn_recomm:before \\{border-color:{=pcol2} !important;\\}\n" + 
				"\n" + 
				"#post-area .u_cbox .u_cbox_mine .u_cbox_name_area,\n" + 
				"#post-area .u_cbox .u_cbox_paginate strong.u_cbox_page .u_cbox_num_page,\n" + 
				"#post-area .commentbox_header .commentbox_pagination .num strong,\n" + 
				"#post-area .post-btn .postre .u_likeit_list_module .u_likeit_list_btn.on,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_btn_recomm_on .u_cbox_ico_recomm:before,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_btn_recomm_on .u_cbox_cnt_recomm,\n" + 
				"#post-area .u_cbox .u_cbox_ico_editor .u_cbox_txt_editor \\{color:{=pcol3} !important;\\}\n" + 
				"\n" + 
				"#post-area .post-btn .postre .u_likeit_list_module .u_likeit_list_btn.on:before,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_btn_recomm_on:before,\n" + 
				"#post-area .u_cbox .u_cbox_ico_editor \\{border-color:{=pcol3} !important;\\}\n" + 
				"\n" + 
				"#post-area .u_cbox .u_cbox_content_wrap:before,\n" + 
				"#post-area .u_cbox .u_cbox_comment_box:before,\n" + 
				"#post-area .u_cbox .u_cbox_comment .u_cbox_write_wrap:before,\n" + 
				"#post-area .u_cbox .u_cbox_tool .u_cbox_btn_reply:before,\n" + 
				"#post-area .u_cbox .u_cbox_work_sub .u_cbox_work_box:before,\n" + 
				"#post-area .u_cbox .u_cbox_ico_reply,\n" + 
				"#post-area .u_cbox .u_cbox_paginate strong.u_cbox_page:before,\n" + 
				"#post-area .commentbox_header:before,\n" + 
				"#post-area .commentbox_header .btn_pagination a:before,\n" + 
				"#post-area .post-btn .postre .u_likeit_list_module .u_likeit_list_btn:before,\n" + 
				"#post-area .u_cbox .u_cbox_recomm_set .u_cbox_btn_recomm:before\n" + 
				"\\{border-color:{=pcol2};\\}\n" + 
				"</script>\n" + 
				"        	\n" + 
				"				\n" + 
				"				\n" + 
				"					\n" + 
				"\n" + 
				"          			\n" + 
				"          				\n" + 
				"		            		\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/global/RemoveSubDomain-12d2f16_https.js\"></script>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var isAutoPlayYN = false;\n" + 
				"\n" + 
				"\n" + 
				"var tagOpenModeType = '0';\n" + 
				"var tagMarkDisplayIfPostTagEmpty = false;\n" + 
				"\n" + 
				"var categoryNo = '12';\n" + 
				"var parentCategoryNo = '0';\n" + 
				"var categoryParentCategoryNo = '';\n" + 
				"var currentCategoryNo;\n" + 
				"if(categoryNo==\"\" && parentCategoryNo == \"\"){ \n" + 
				"	currentCategoryNo = 0;\n" + 
				"}else if((categoryNo==\"\" || categoryNo==\"0\") && parentCategoryNo != \"\"){  \n" + 
				"	currentCategoryNo = parentCategoryNo;\n" + 
				"}else if(categoryNo!=\"\"){  \n" + 
				"	currentCategoryNo = categoryNo;\n" + 
				"}\n" + 
				"\n" + 
				"var viewDate = '' || ''; \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var isFromSearch = false;	\n" + 
				"var isFromSection = true;	\n" + 
				"\n" + 
				"var forFocusingTargetId = \"category-name\";\n" + 
				"\n" + 
				"var isFirstPage = false;	\n" + 
				"var from = 'section';	\n" + 
				"var search_logNo = \"223264915610\";\n" + 
				"\n" + 
				"var postListTopCurrentPage = '1';\n" + 
				"var categoryName = \"%EC%8B%9C%EA%B3%B5+%ED%8F%AC%ED%8A%B8%ED%8F%B4%EB%A6%AC%EC%98%A4\";\n" + 
				"\n" + 
				"var gsGfMarketUrl = 'http://m.gfmarket.naver.com';\n" + 
				"\n" + 
				"\n" + 
				"var userTopListCurrentPage = \"\";\n" + 
				"var userTopListCount = \"\";\n" + 
				"var userTopListOpen = false; \n" + 
				"var userTopListManageOpen = false; \n" + 
				"var isTopListOpen = true; \n" + 
				"var topListPostCount = \"5\"; \n" + 
				"\n" + 
				"var isTopListCategoryEmpty = false;\n" + 
				"if (!isTopListCategoryEmpty) {\n" + 
				"	isTopListOpen = true; \n" + 
				"	topListPostCount = \"5\"; \n" + 
				"}\n" + 
				"\n" + 
				"var isThumbnailList = false;\n" + 
				"var isThumbnailView = true;\n" + 
				"\n" + 
				"var aPostSendInfo = [];\n" + 
				"var aPostBaseInfo = [];\n" + 
				"var aPostFiles = [];\n" + 
				"var aPostVideoInfo = [];\n" + 
				"var aPostImageFileSizeInfo = [];\n" + 
				"var aPostAutoSourcingHtmlView = [];\n" + 
				"var aQueuePost = []; \n" + 
				"var htInitData = [];\n" + 
				"\n" + 
				"var paramLogNo = '223264915610';\n" + 
				"var blogId = 'wood-24';\n" + 
				"var isAfterWrite = false;\n" + 
				"\n" + 
				"\n" + 
				"var isRecommendedBlog = false;\n" + 
				"\n" + 
				"var gdidTag = [];\n" + 
				"var g_ssc = \"blog.post\";\n" + 
				"var nFirstLogNo = \"\";\n" + 
				"var pagePerPostCount = 1;\n" + 
				"\n" + 
				"\n" + 
				"var sCommentNoPosition = '';	\n" + 
				"var isCommentAreaOpen = false;\n" + 
				"var postBottomOptionalAreaType = '0';\n" + 
				"var commentAreaOpenType = '1';\n" + 
				"var isRelayAreaOpen = false;\n" + 
				"var isSympathyAreaOpen = false;\n" + 
				"var relayAreaOpenType = '2';\n" + 
				"var firstPostLogNo = \"223264915610\";\n" + 
				"var firstPostIsAllOpenPost = true;\n" + 
				"var searchImg = '';\n" + 
				"var searchVid = '';\n" + 
				"var blockedBlog = 'false';\n" + 
				"var videoAutoPlay = blockedBlog === 'false';\n" + 
				"\n" + 
				"\n" + 
				"var currentPage = '1';\n" + 
				"var tagParam = '' || '&logNo=223264915610';\n" + 
				"\n" + 
				"\n" + 
				"var imageWidth = '' || '550';\n" + 
				"\n" + 
				"\n" + 
				"var maxNoticePostCount = '5';\n" + 
				"var gbIsPostCommentOpen = false;\n" + 
				"\n" + 
				"\n" + 
				"var isDoPostAggregate = false;\n" + 
				"\n" + 
				"\n" + 
				"var apiGwUrl = 'https://apis.naver.com/';\n" + 
				"var likeItVideoEnable = true;\n" + 
				"var likeItVideoIdListJson = '[\"26B7CF336CFD6A71510B294117F8B8263260\"]';\n" + 
				"var likeItVideoContentsIdMapJson = '{\"26B7CF336CFD6A71510B294117F8B8263260\":\"wood-24_223264915610_26B7CF336CFD6A71510B294117F8B8263260\"}';\n" + 
				"var isVideoplayerUseProxy = true;\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"var blogNicknameOrBlogId = '우드24';\n" + 
				"\n" + 
				"\n" + 
				"var videoPlayerUrl = \"https://ssl.pstatic.net/t.static.blog/mylog/versioning//lib/prismplayer/prismplayer-pc-0.4.5.min-10e993e_https.js\";\n" + 
				"var advertiseInfoUrl = 'https://api.tv.naver.com';\n" + 
				"var isDevPhase = \"false\";\n" + 
				"var isStagePhase = \"false\";\n" + 
				"var isNotHasSe3AndSeOne = false;\n" + 
				"var isScrap = 'false' === 'true';\n" + 
				"\n" + 
				"var postAdvertiseEnableYN = true;\n" + 
				"\n" + 
				"var rproductId = '';\n" + 
				"var bLoginUser = 'false' === 'true';\n" + 
				"var isVideoSubtitleInsertEnable = 'false'  === 'true';\n" + 
				"\n" + 
				"var userNaverId = '';\n" + 
				"var showCoupon = 'false' === 'true';\n" + 
				"var gPcUrl = 'https://blog.naver.com';\n" + 
				"var hasProduct = 'false' === 'true';\n" + 
				"var feEnv = 'real';\n" + 
				"var infEnv = 'real';\n" + 
				"var	gbIsInf = \"\" == \"true\" ? true : false;\n" + 
				"var endPageProductMap = JSON.parse('{\"223264915610\":{\"sellerNo\":0,\"hasProduct\":false,\"secedeSellerHasProduct\":false,\"productList\":[],\"delivery\":{\"merchantName\":\"\",\"deliveryCompany\":\"\",\"returnShippingFee\":0,\"exchangeShippingFee\":0,\"address1\":\"\",\"address2\":\"\",\"zipCode\":\"\",\"phoneNumber\":\"\",\"feePrice\":0,\"conditionalFree\":0,\"etcDeliveryFee\":0},\"postProductStatus\":null,\"leaveRequestSeller\":false,\"tradeInterruptionSeller\":false,\"temporaryStopSeller\":false}}');\n" + 
				"var feMobileUrl= \"https://fe-m.blog.naver.com\";\n" + 
				"\n" + 
				"var blogModuleJs= \"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/post/market/blogModule-ebe2c0e_https.js\";\n" + 
				"\n" + 
				"var itemFactoryDomainDisabled = \"true\" === \"true\";\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	gUserId = \"\";\n" + 
				"	gBlogId = \"wood-24\";\n" + 
				"	gSE3Url = \"https://blog.editor.naver.com/\";\n" + 
				"    gsSesseionId = \"0A222EBD618C3ACBCFA9B30DFA18B4B1.jvm1\";\n" + 
				"    gsSearchKeyword = \"\";\n" + 
				"    gsBlogOwnerYn = false? \"Y\" : \"N\";\n" + 
				"    \n" + 
				"    gIsRabbitWriterOn = 'true';\n" + 
				"	gsSaInfo = \"\";\n" + 
				"	gbIsNotOpenBlogUser = \"false\" === \"true\";\n" + 
				"	gUserNaverId = \"\";\n" + 
				"\n" + 
				"	\n" + 
				"	\n" + 
				"		nFirstLogNo = \"223264915610\";		\n" + 
				"        gnFirstLogNo = \"223264915610\";\n" + 
				"        gsFirstCategoryName = \"\\uC2DC\\uACF5 \\uD3EC\\uD2B8\\uD3F4\\uB9AC\\uC624\";\n" + 
				"\n" + 
				"	\n" + 
				"	\n" + 
				"	gdidTag[1] = \"90000003_0000000000000033FBA0409A\";\n" + 
				"	aPostAutoSourcingHtmlView[1] = \"\";\n" + 
				"	\n" + 
				"	\n" + 
				"		aPostFiles[1] = JSON.parse('[]'.replace(/\\\\'/g, ''));\n" + 
				"	\n" + 
				"	aPostVideoInfo[1] = [ {\"belongedLogNo\": \"223264915610\", \"vid\" : \"26B7CF336CFD6A71510B294117F8B8263260\" ,\"hasLink\": true }];\n" + 
				"	\n" + 
				"	aPostImageFileSizeInfo[1] = '{\\'\\/MjAyMzExMTRfMTQy\\/MDAxNjk5OTIzMzk2NDM0.qiC0sQoWaVhJIRP6OD_yG7jcUonzN3OVW3PrHJoa5vYg.Q8YlCv0WqRbk9KMPu9v9IYdbGihNvICGso9qzCjXE4Qg.JPEG.wood-24\\/%E5%AA%9B%EB%BA%A3%EA%B6%93_%EF%BF%BD%EA%B8%BD%E5%AA%9B%EF%BF%BD%EF%BF%BD%EB%9C%B2%EF%BF%BD%EA%B2%95_%EF%BF%BD%EC%A0%A3%EF%BF%BD%EC%98%89_20.jpg\\': \\'2,541,384|%E5%AA%9B%EB%BA%A3%EA%B6%93_%EF%BF%BD%EA%B8%BD%E5%AA%9B%EF%BF%BD%EF%BF%BD%EB%9C%B2%EF%BF%BD%EA%B2%95_%EF%BF%BD%EC%A0%A3%EF%BF%BD%EC%98%89_20.jpg\\' , \\'\\/MjAyMzExMTRfNCAg\\/MDAxNjk5OTI1NDQ4ODgx.FpbOmM7NS9gMWQX6HTQ00UH9wXWxn-7lsTrkgGw5Fgkg.65C568OvtxMQeJp2W89lHQnGEHp5wQy1aVS3SsTEZmkg.PNG.wood-24\\/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9.png\\': \\'1,025,857|%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9.png\\' , \\'\\/MjAyMzExMTRfMjUx\\/MDAxNjk5OTI3NzkxMTQ0.rRLX2CjekCViqiNxqs4KTb_Fr_DahelM3WYpIv29jmgg.MMFe39hBNVN4zS3XeF0zQJypEL_n59o1r0QFQvcvZrUg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91.jpg\\': \\'1,998,533|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91.jpg\\' , \\'\\/MjAyMzExMTRfMTQ3\\/MDAxNjk5OTI3OTEwNTM0.Jm9N4gGPuNZxBUqEOX2xAyCApYyeLhipGMy-lYjIsXMg.SF33b5nPFQ29zJg-6ICRGYE6qWa_YWDDRNAdZPojGDgg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_3.jpg\\': \\'2,461,815|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_3.jpg\\' , \\'\\/MjAyMzExMTRfNTIg\\/MDAxNjk5OTI5MTUzMjUw.kHJFJtl5zckxdVjED15ZxUUY3rxDz-i-NSl5SwPxS2kg.naKuui-hDOI_TL6Eps-NpDLGZjY7esYx8rij5lnsA8Yg.JPEG.wood-24\\/%EC%98%A4%EC%9D%BC%EC%8A%A4%ED%85%8C%EC%9D%B8.jpg\\': \\'3,072,396|%EC%98%A4%EC%9D%BC%EC%8A%A4%ED%85%8C%EC%9D%B8.jpg\\' , \\'\\/MjAyMzExMTRfMTc4\\/MDAxNjk5OTQ0ODQ3MDQ3.sBAtVumOhMkziu4HbcljxUlZLYaZd_UjgV6G1tO7OV0g.Rfyc5h1AP6Fmtfhape3qA6oaIG-HXw4SVCj93HAnK3wg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_5.jpg\\': \\'3,397,119|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_5.jpg\\' , \\'\\/MjAyMzExMTRfMiAg\\/MDAxNjk5OTI3OTEwOTgz.A4jZvUAi9EfBJZY2P3zJRSzLAhaRDJu_L4BlIPm-io4g.8SrwJns4Myk0BNuIMIloBnz4wVOmcOOx4DV-ibE3NyIg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_6.jpg\\': \\'2,370,359|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_6.jpg\\' , \\'\\/MjAyMzExMTRfNDAg\\/MDAxNjk5OTQ2MzQzNzM2.L5MKuiKAs_hI7Sfi65Y7KnUJ-aaLIKE8uRcXSenkOWAg.qnlckhez1-ngqGIrToSH2m3DH16djFSPmsbUfxxZ6ZIg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_10.jpg\\': \\'2,163,597|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_10.jpg\\' , \\'\\/MjAyMzExMTRfMTM4\\/MDAxNjk5OTQ2MzQ1MzY5.Uh87K2gBT-tq51j0E600fNNSIf84gaPRskVToUFcmXUg.QY3xGyr4bPhgkfdI-m-4fq1bbJPr72mge7axMcD_hrgg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_8.jpg\\': \\'2,461,468|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_8.jpg\\' , \\'\\/MjAyMzExMTRfMjU2\\/MDAxNjk5OTYxOTQ2NDIw.PV6t7gnPvXrEyzKywIinasa8VuQdEbLpOSMopWh3elcg.3Z93rSqsyR435UpNzpN3t9wrhJB7Xib3OquzbWACjBcg.JPEG.wood-24\\/%EC%9D%80%EB%B6%84%EC%8A%A4%ED%94%84%EB%A0%88%EC%9D%B4.jpg\\': \\'210,302|%EC%9D%80%EB%B6%84%EC%8A%A4%ED%94%84%EB%A0%88%EC%9D%B4.jpg\\' , \\'\\/MjAyMzExMTRfMTg4\\/MDAxNjk5OTQ2MzQ0NzIx.IRHCSMMmQSsZvFrmmhteUrq5S-YCXnhkOg0-zvlrux4g.zDC1Kc5PMGoQe1d5oiaF4tjHndhBwHgFC4uJMh62GY4g.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_7.jpg\\': \\'2,020,734|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_7.jpg\\' , \\'\\/MjAyMzExMTRfMTUx\\/MDAxNjk5OTYyMzY2Nzg3.mNKxBzxLZCNwioKWRFMxwZ8gxvqcbWE1g86_373wPbEg.2J5jgXmOyE6blR987UIDfVO1SEluiYMqWLC3YraMiwgg.PNG.wood-24\\/image.png\\': \\'65,767|image.png\\' , \\'\\/MjAyMzExMTRfMjQ2\\/MDAxNjk5OTYzMTExMTk4.VjVMwHqRjCPdWzYvICwCLLIGKSUsD6O2Yn5FKtG24J4g.UHQ0VvewL9LFJT3rLevFdoDACdSANt054di0ESyDS4Eg.JPEG.wood-24\\/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9_2.jpg\\': \\'56,939|%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9_2.jpg\\' , \\'\\/MjAyMzExMTRfMzEg\\/MDAxNjk5OTY2OTM4NTE3.qKe82Qq00wUS9sprLLEi_zi4yxs46RyYxjn9UvW5JOUg.pXv7x86wkbXZhjZNx-jlalx5LHZoSuc3fDoATHUbDfQg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_13.jpg\\': \\'2,169,593|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_13.jpg\\' , \\'\\/MjAyMzExMTRfMzMg\\/MDAxNjk5OTY2OTMwNzgw.1xu_Yg0hcMWDyf203AcZZgOUIylKefWUyQaTquJO070g.-Kwy66x1J5A3NBjhCQ20gTHFZJrM4rM19Nhiz_SWNhYg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_19.jpg\\': \\'2,792,863|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_19.jpg\\' , \\'\\/MjAyMzExMTRfMTA2\\/MDAxNjk5OTY3MzkwMTkz.TCITRJ3-vzeX8dBf-EhGy5b6t4YniVfadpw2MeLUmSIg.dJvUStE86ZRrNJgEiZUFC3wPY_iDiKRKiiTxrl04XXog.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_22.jpg\\': \\'3,818,797|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_22.jpg\\' , \\'\\/MjAyMzExMTRfMTM5\\/MDAxNjk5OTY2OTM3ODM2.KlNoUaaQYJRxAvGWnfu5wCHPryhomL2M4CkmNIBxRZAg.r2l30OKmNHz5HvYFMfWNSUbQM-SOlmLQM8j7Qe6bovgg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg\\': \\'2,633,267|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg\\' , \\'\\/MjAyMzExMTRfMjY0\\/MDAxNjk5OTY2OTM1MzYz.fgdGx3lFjO6-MSZeeV3pI2nOjoO8rDREAC9Ag6c6FK0g.IdaXlBamSC5w5N2f64sBCqs3pI3xr-xbx7-uz4-ET-cg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_17.jpg\\': \\'2,359,407|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_17.jpg\\' , \\'\\/MjAyMzExMTRfMjcw\\/MDAxNjk5OTY2OTk2OTIx.DkTQoxjqaH6rtkLReTO7AoPIeYt21YcZp05AX4sRSosg.PlQhGglISRAy9AXwZ68DzRz7E_n9ZbJRBpgSUhBKCnEg.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_16.jpg\\': \\'2,591,704|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_16.jpg\\' , \\'\\/MjAyMzExMTRfMjk4\\/MDAxNjk5OTY3MTUwODQw.qcXsxLVn1D7oPz5JlfRqLFNcoDv5wD9OkeVXNXZ3q2wg.afy7WUlDzms1GGlzU9RIS59MW_q3vfNNB0vtCGZxPEog.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_21.jpg\\': \\'3,081,237|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_21.jpg\\' , \\'\\/MjAyMzExMTRfMTAx\\/MDAxNjk5OTY4Nzc5MzYz.Z9zF4FxlC9d_diBEtnrK93m1m6o8vsfz1uOUY3eESWAg.W03MEsZiZsMvmrQp2dRjf48Vnnfxkjq-nvvyXRTzR7Ig.JPEG.wood-24\\/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg\\': \\'132,776|%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg\\'}';\n" + 
				"	aPostSendInfo[1] = [];\n" + 
				"	aPostBaseInfo[1] = \"223264915610|0|1|1|12|0|false|4|MYLOG\";\n" + 
				"	aQueuePost.push({logNo : '223264915610',tagNames : ''}); 	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"//<!--\n" + 
				"	htInitData[0] = {\n" + 
				"		\n" + 
				"		style  : \"community\",\n" + 
				"		\n" + 
				"		\n" + 
				"		textClass : \"pcol2\",\n" + 
				"		barClass : \"fil3 pcol2b\",\n" + 
				"		align : \"right\",\n" + 
				"		onLoginRedirect : function() {window.open(\"https://nid.naver.com/nidlogin.login?svctype=16448&url=\" + encodeURIComponent(blogURL + \"/post/common/reloadOpenerParentAndClose.jsp\"), \"\", 'width=400px, height=500px, scrollbars=yes, resizable=yes');},\n" + 
				"\n" + 
				"		evkey  : \"blog\",\n" + 
				"		servicename : \"블로그\",\n" + 
				"		title  : \"\\uAC15\\uB0A8 \\uC0C1\\uAC00\\uB370\\uD06C \\uC81C\\uC791 \\uACFC\\uC815(\\uC778\\uC870 \\uBC29\\uBD80\\uBAA9 \\uC0AC\\uC6A9)\",\n" + 
				"		source : \"https://blog.naver.com/wood-24/223264915610\",\n" + 
				"		me2key : \"\",\n" + 
				"		tags   : [\"네이버블로그\", \"\\uC6B0\\uB4DC24\"],\n" + 
				"\n" + 
				"\n" + 
				"		me : {\n" + 
				"			\n" + 
				"			feedInfo : \"블로거의 글이\",\n" + 
				"			popupType : \"C\",\n" + 
				"			targetUrl : \"/subscribe/blog/applyFeedRelation.nhn\",\n" + 
				"			serviceId : \"blog\",\n" + 
				"			feedGroupId : \"neighbor\",\n" + 
				"			feedId : [\"wood-24\"],\n" + 
				"			feedName : [\"\\uC6B0\\uB4DC24\"],\n" + 
				"			feedUrl : [\"https://blog.naver.com/wood-24\" ],\n" + 
				"			selected : ['Y']\n" + 
				"		},\n" + 
				"		\n" + 
				"\n" + 
				"		\n" + 
				"		\n" + 
				"		\n" + 
				"		me2day : {sourceUrl : \"https://blog.naver.com/PostView.naver?blogId=wood-24&logNo=223264915610\"},\n" + 
				"		\n" + 
				"		\n" + 
				"\n" + 
				"		\n" + 
				"		\n" + 
				"		\n" + 
				"		mail : {\n" + 
				"			srvid   : \"blog\",\n" + 
				"			srvurl  : \"http://bookmark.naver.com/getRichMailTmpl.ajax?sourceUrl=\"\n" + 
				"			 + encodeURIComponent(\"https://blog.naver.com/PostView.naver?blogId=wood-24&logNo=223264915610\")\n" + 
				"		},\n" + 
				"		facebook : {sourceUrl : \"https://blog.naver.com/PostView.naver?blogId=wood-24&logNo=223264915610\"},\n" + 
				"		\n" + 
				"		\n" + 
				"\n" + 
				"		cafe : {\n" + 
				"			proxyUrl : \"/ScrapToCafe.naver?blogId=wood-24&logNo=223264915610&inAppNavigation=true\" \n" + 
				"			\n" + 
				"		},\n" + 
				"		blog : {\n" + 
				"			\n" + 
				"			proxyUrl : \"/ScrapToBlog.naver?blogId=wood-24&logNo=223264915610&loadOnModalView=true&template=modal&isShowingGnb=false\"\n" + 
				"		}\n" + 
				"	};\n" + 
				"\n" + 
				"\n" + 
				"// 스크립트 오류를 방지하지 위해서 막아논 부분.\n" + 
				"//-->\n" + 
				"function splugin_oninitialize(nCount) {\n" + 
				"	return htInitData[nCount - 1];\n" + 
				"}\n" + 
				"</script>\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/PostTopCommon-462369321_https.js\" charset=\"UTF-8\"></script>\n" + 
				"\n" + 
				"		\n" + 
				"\n" + 
				"	\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div class=\"division-line-x\" id=\"quickPostTopLine\" style=\"display:none\"></div>\n" + 
				"<div class=\"qk_wrap\"  id=\"quickPostBanner\" style=\"display:none\" onclick=\"nclk_v2(this,'qed.open','','');\">\n" + 
				"	<div class=\"qk_post\">\n" + 
				"		<p class=\"msg _qpMessage _postSize _rosRestrictAll\" style=\"font-size:12px\"></p>\n" + 
				"		<button type=\"button\" class=\"qkb_post _postSize _rosRestrictAll\"><span>글쓰기</span></button>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<div id=\"quickPostPromotion\" class=\"qk_layer2\" style=\"display: none\">\n" + 
				"	<div class=\"qk_layer2_inner\">\n" + 
				"		<span class=\"blind\">가벼운 글쓰기툴 퀵에디터가 오픈했어요!</span>\n" + 
				"		<a href=\"https://admin.blog.naver.com//config/quickposting\" target=\"_top\" style=\"top:8px;left:187px\"><img src=\"https://blogimgs.pstatic.net/nblog/quickeditor/btn_qk_set.gif\" width=\"65\" height=\"14\" alt=\"사용설정하기\"></a>\n" + 
				"		<a href=\"https://section.blog.naver.com/Notice.naver?docId=10000000000020964978\" target=\"_blank\" style=\"top:8px;right:32px\"><img src=\"https://blogimgs.pstatic.net/nblog/quickeditor/btn_what2.gif\" width=\"57\" height=\"14\" alt=\"퀵에디터란?\"></a>\n" + 
				"\n" + 
				"		<a href=\"#\" class=\"clse _returnFalse _closeQuickPostPromotion\"><img src=\"https://blogimgs.pstatic.net/nblog/quickeditor/btn_clse_ly2.gif\" width=\"15\" height=\"15\" alt=\"설정 닫기\"></a>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\" id=\"quickPostBottomLine\" style=\"display:none\"></div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"    \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<DIV class=post><DIV class=post-back>\n" + 
				"<TABLE class=post-head cellSpacing=0 cellPadding=0>\n" + 
				"<TBODY>\n" + 
				"<TR>\n" + 
				"<TD class=htl noWrap></TD>\n" + 
				"<TD class=htc></TD>\n" + 
				"<TD class=htr noWrap></TD></TR></TBODY></TABLE>\n" + 
				"<TABLE class=post-body cellSpacing=0 cellPadding=0>\n" + 
				"<caption>\n" + 
				"	<span class=\"blind\">공지 목록</span>\n" + 
				"</caption>\n" + 
				"<TBODY>\n" + 
				"<TR>\n" + 
				"<TD class=bcl noWrap></TD>\n" + 
				"<TD class=bcc>\n" + 
				"\n" + 
				"	<div class=\"wrap_blog2_list wrap_blog2_notice\">\n" + 
				"		<table cellspacing=\"0\" cellpadding=\"0\" class=\"blog2_list blog2_notice\">\n" + 
				"			<caption><span class=\"blind\">공지글</span></caption>\n" + 
				"			<colgroup>\n" + 
				"				<col class=\"title\">\n" + 
				"				<col class=\"date\">\n" + 
				"			</colgroup>\n" + 
				"			<thead>\n" + 
				"			<tr>\n" + 
				"				<th scope=\"col\" class=\"blind\">글 제목</th>\n" + 
				"				<th scope=\"col\" class=\"blind\">작성일</th>\n" + 
				"			</tr>\n" + 
				"			</thead>\n" + 
				"			<tbody>\n" + 
				"			\n" + 
				"			<tr class=\"\">\n" + 
				"				<td class=\"title\">\n" + 
				"					<div class=\"wrap_td\">\n" + 
				"					\n" + 
				"						\n" + 
				"						\n" + 
				"							<div class=\"meta_data\">\n" + 
				"								<span class=\"num pcol3\">(18)</span>\n" + 
				"							</div>\n" + 
				"							<span class=\"ell2 pcol2\">\n" + 
				"								\n" + 
				"								<strong class=\"pcol3 notice\">공지</strong>\n" + 
				"								<a href=\"/PostView.naver?blogId=wood-24&logNo=221403936510&categoryNo=13&parentCategoryNo=-1&viewDate=&currentPage=&postListTopCurrentPage=&isAfterWrite=true\" class=\"pcol2\" onclick=\"nclk_v2(this,'not.notice','','');\">이것만 보면 하자없다 우드24 합성목재 데크 시방서</a>\n" + 
				"							</span>\n" + 
				"						\n" + 
				"					\n" + 
				"					</div>\n" + 
				"				</td>\n" + 
				"				<td class=\"date\">\n" + 
				"					<span class=\"date pcol2\">2018. 11. 25.</span>\n" + 
				"				</td>\n" + 
				"			</tr>\n" + 
				"			\n" + 
				"			<tr class=\"\">\n" + 
				"				<td class=\"title\">\n" + 
				"					<div class=\"wrap_td\">\n" + 
				"					\n" + 
				"						\n" + 
				"						\n" + 
				"							<div class=\"meta_data\">\n" + 
				"								<span class=\"num pcol3\">(240)</span>\n" + 
				"							</div>\n" + 
				"							<span class=\"ell2 pcol2\">\n" + 
				"								\n" + 
				"								<strong class=\"pcol3 notice\">공지</strong>\n" + 
				"								<a href=\"/PostView.naver?blogId=wood-24&logNo=222530916785&categoryNo=15&parentCategoryNo=-1&viewDate=&currentPage=&postListTopCurrentPage=&isAfterWrite=true\" class=\"pcol2\" onclick=\"nclk_v2(this,'not.notice','','');\">합성목재 데크 시공 시 이런 업체는 반드시 피하자!</a>\n" + 
				"							</span>\n" + 
				"						\n" + 
				"					\n" + 
				"					</div>\n" + 
				"				</td>\n" + 
				"				<td class=\"date\">\n" + 
				"					<span class=\"date pcol2\">2021. 10. 12.</span>\n" + 
				"				</td>\n" + 
				"			</tr>\n" + 
				"			\n" + 
				"			</tbody>\n" + 
				"		</table>\n" + 
				"	</div>\n" + 
				"\n" + 
				"</TD>\n" + 
				"<TD class=bcr noWrap></TD></TR></TBODY></TABLE>\n" + 
				"<TABLE class=post-footer cellSpacing=0 cellPadding=0>\n" + 
				"<TBODY>\n" + 
				"<TR>\n" + 
				"<TD class=ftl noWrap></TD>\n" + 
				"<TD class=ftc></TD>\n" + 
				"<TD class=ftr noWrap></TD></TR></TBODY></TABLE></DIV></DIV>\n" + 
				"<DIV class=\"division-line-x plile\"></DIV>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div class=\"post post_top_title_aggregate_expose post_top_title_aggregate_click\" id=\"category-name\" >\n" + 
				"	<div class=\"post-back\">\n" + 
				"		<table class=\"post-head\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\n" + 
				"			<tbody><tr>\n" + 
				"				<td class=\"htl\" nowrap=\"nowrap\"></td><td class=\"htc\"></td><td class=\"htr\" nowrap=\"nowrap\"></td>\n" + 
				"			</tr>\n" + 
				"			</tbody></table>\n" + 
				"		</table>\n" + 
				"		<table class=\"post-body\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\n" + 
				"			<tbody><tr>\n" + 
				"				<td class=\"bcl\" nowrap=\"nowrap\"></td>\n" + 
				"				<td class=\"bcc\">\n" + 
				"					\n" + 
				"						\n" + 
				"						\n" + 
				"							\n" + 
				"								\n" + 
				"								\n" + 
				"									 \n" + 
				"							\n" + 
				"						\n" + 
				"					\n" + 
				"					<div class=\"wrap_blog2_list wrap_blog2_categorylist\">\n" + 
				"						<h4 class=\"category_title pcol2\">\n" + 
				"							<a href=\"#\" role=\"button\" class=\"pcol2 _toggleTopList _returnFalse\" title=\"시공 포트폴리오 목록 닫고 열기\" id =\"categoryTitle\"><strong>시공 포트폴리오</strong></a> 269개의 글\n" + 
				"						</h4>\n" + 
				"						<a href=\"#\" class=\"btn_openlist pcol2 _toggleTopList _returnFalse\" role=\"button\"><span class=\"blind\">시공 포트폴리오</span><span class=\"txt\" id=\"toplistSpanBlind\">목록열기</span></a>\n" + 
				"						<div class=\"wrap_list\" area-hidden=\"false\" id=\"toplistWrapper\" style=\"display:none;\"></div>\n" + 
				"					</div>\n" + 
				"\n" + 
				"\n" + 
				"				</td>\n" + 
				"				<td class=\"bcr\" nowrap=\"nowrap\"></td>\n" + 
				"			</tr>\n" + 
				"			</tbody></table>\n" + 
				"		<table class=\"post-footer\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">\n" + 
				"			<tbody><tr><td class=\"ftl\" nowrap=\"nowrap\"></td><td class=\"ftc\"></td><td class=\"ftr\" nowrap=\"nowrap\"></td></tr>\n" + 
				"			</tbody></table>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x plile\"></div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"	\n" + 
				"		\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"postListBody\">\n" + 
				"	\n" + 
				"	\n" + 
				"	<div id=\"post_1\" class=\"post _post_wrap _param(1)\" data-post-editor-version=\"4\">\n" + 
				"		<div class=\"post-back\">\n" + 
				"			<table class=\"post-head\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"htl\" nowrap=\"nowrap\"></td><td class=\"htc\"></td><td class=\"htr\" nowrap=\"nowrap\"></td></tr></table>\n" + 
				"			<table id=\"printPost1\" class=\"post-body\" cellspacing=\"0\" cellpadding=\"0\" role='presentation'>\n" + 
				"				<tr>\n" + 
				"					<td class=\"bcl\" nowrap=\"nowrap\"></td>\n" + 
				"                    <td class=\"bcc\">\n" + 
				"						\n" + 
				"						\n" + 
				"						\n" + 
				"						\n" + 
				"						\n" + 
				"                        \n" + 
				"                    	\n" + 
				"						<div id=\"post-view223264915610\" class=\"wrap_rabbit pcol2 _param(1) _postViewArea223264915610\">\n" + 
				"							<!-- Rabbit HTML --><div class=\"se-viewer se-theme-default\" lang=\"ko-KR\">\n" + 
				"    <!-- SE_DOC_HEADER_START -->\n" + 
				"    <div class=\"se-component se-documentTitle se-l-default  se-documentTitle-cover-image\" id=\"SE-71474fa7-bc13-4d29-a312-b619265cb828\">\n" + 
				"        <div class=\"se-title-cover\" style=\"background-image:url('https://postfiles.pstatic.net/MjAyMzExMTRfMTQy/MDAxNjk5OTIzMzk2NDM0.qiC0sQoWaVhJIRP6OD_yG7jcUonzN3OVW3PrHJoa5vYg.Q8YlCv0WqRbk9KMPu9v9IYdbGihNvICGso9qzCjXE4Qg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_20.jpg?type=w580'); background-position: 50.0% 50.0%;\">\n" + 
				"            <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTQy/MDAxNjk5OTIzMzk2NDM0.qiC0sQoWaVhJIRP6OD_yG7jcUonzN3OVW3PrHJoa5vYg.Q8YlCv0WqRbk9KMPu9v9IYdbGihNvICGso9qzCjXE4Qg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_20.jpg?type=w580\" alt=\"\" class=\"se-title-cover-exception-image\" />\n" + 
				"        </div>\n" + 
				"        <div class=\"se-component-content\">\n" + 
				"            <div class=\"se-section se-section-documentTitle se-l-default se-section-align-center\">\n" + 
				"                <!-- --> \n" + 
				"<div class=\"blog2_series\">\n" + 
				"	<a href=\"/PostList.naver?blogId=wood-24&categoryNo=12&from=postList&parentCategoryNo=12\" class=\"pcol2\" onclick=\"nclk_v2(this,'pst.category','','');\">시공 포트폴리오</a>\n" + 
				"</div>\n" + 
				"<div class=\"pcol1\"> \n" + 
				"<!-- -->\n" + 
				"                <div class=\"se-module se-module-text se-title-text\">\n" + 
				"                    <p class=\"se-text-paragraph se-text-paragraph-align-center\" style=\"\" id=\"SE-e258ca4d-3a07-43dc-8113-a1f68ec0a4ca\"><span style=\"\" class=\"se-fs- se-ff-system\" id=\"SE-0d0b57e4-461f-445e-8d9b-1374be584283\"><!-- -->강남 상가데크 제작 과정(인조 방부목 사용)<!-- --></span></p>                </div>\n" + 
				"                <!-- -->\n" + 
				"</div>\n" + 
				"<div class=\"blog2_container\">\n" + 
				"    <span class=\"writer\">\n" + 
				"        <span class=\"area_profile\"><a href=\"https://blog.naver.com/wood-24\" class=\"link\" onclick=\"nclk_v2(this,'pst.profile','','');\" target=\"_top\"><img src=\"https://blogpfthumb-phinf.pstatic.net/MjAxODExMTBfODcg/MDAxNTQxODMzMjg0NzUy.D64-EQ1hrmaSPKFGT0jSt5t4EK7cFn1lZnbf-rwsSg8g.D5Dss_alDxNrB-aYWxnI0FBtVTMPuFwY-58EexTl2uQg.JPEG.wood-24/%25BB%25F3%25C8%25A3.jpg?type=s1\" class=\"img\" alt=\"프로파일\"></a></span>\n" + 
				"        <span class=\"nick\"><a href=\"https://blog.naver.com/wood-24\" class=\"link pcol2\" onclick=\"nclk_v2(this,'pst.username','','');\" target=\"_top\">우드24</a></span>\n" + 
				"    </span>\n" + 
				"    <i class=\"dot\"> ・ </i>\n" + 
				"	<span class=\"se_publishDate pcol2\">4시간 전</span>\n" + 
				"\n" + 
				"</div>\n" + 
				"<div class=\"blog2_post_function\">\n" + 
				"	<a href=\"#\" id=\"copyBtn_223264915610\" class=\"url pcol2 _setClipboard _returnFalse _se3copybtn _transPosition\" title=\"https://blog.naver.com/wood-24/223264915610\" style=\"cursor:pointer;\" >URL 복사</a>\n" + 
				"\n" + 
				"            <a href=\"#\" class=\"btn_buddy btn_addbuddy pcol2 _buddy_popup_btn _returnFalse\" onclick=\"nclk_v2(this,'pst.addnei','','');\"><i class=\"ico\"></i> 이웃추가<i class=\"aline\"></i></a>\n" + 
				"	<div class=\"overflow_menu\" >\n" + 
				"        <a href=\"#\" class=\"btn_overflow_menu _open_overflowmenu pcol2 _param(223264915610) _returnFalse\" role=\"button\" area-haspopup=\"true\" area-expanded=\"false\"><span class=\"blind\">본문 기타 기능</span></a>\n" + 
				"		<div id=\"overflowmenu-223264915610\" class=\"lyr_overflow_menu\" area-hidden=\"true\">\n" + 
				"               <a href=\"#\" onclick=\"return false;\" id=\"_title_spiButton\" class=\"naver-splugin btn_splugin share _title_share\"\n" + 
				"                  data-style=\"unity\"\n" + 
				"                  data-url=\"https://blog.naver.com/wood-24/223264915610\"\n" + 
				"                  data-oninitialize=\"splugin_oninitialize(1);\"\n" + 
				"                  data-me-display=\"off\"\n" + 
				"                  data-likeServiceId=\"BLOG\"\n" + 
				"                  data-likeContentsId=\"wood-24_223264915610\"\n" + 
				"                  data-logDomain=\"https://proxy.blog.naver.com/spi/v1/api/shareLog\"\n" + 
				"                  data-canonical-url=\"https://blog.naver.com/wood-24/223264915610\"\n" + 
				"                  data-option=\"{baseElement:'_title_spiButton', layerPosition:'outside-bottom', align:'right', marginLeft:0, marginTop:4}\">\n" + 
				"                   공유하기\n" + 
				"                <span class=\"ico_share _title_share_icon\"></span>\n" + 
				"               </a>\n" + 
				"                <a href=\"#\" class=\"_report _param(https://srp2.naver.com/report?svc=BLG&exit=close&ctype=AA01&cwriterenc=V6jE%2F71k3fwCGK4ptHxPKLLH5vZE817V3aMxC6L%2FBnc%3D&ctitle=%EA%B0%95%EB%82%A8%20%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC%20%EC%A0%9C%EC%9E%91%20%EA%B3%BC%EC%A0%95(%EC%9D%B8%EC%A1%B0%20%EB%B0%A9%EB%B6%80%EB%AA%A9%20%EC%82%AC%EC%9A%A9)&cwriter=wood***&dark=disable&memtype=Y&env=pc&cnickname=wood***&vsvc=BLG&cid=wood-24%40%40131720049%40%40mylog%40%40223264915610) _returnFalse\">신고하기<span class=\"ico_report\"></span></a>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"    <input type=\"text\" value=\"https://blog.naver.com/wood-24/223264915610\" alt=\"url\" class=\"copyTargetUrl\" style=\"display:none;\" title=\"URL 복사\">\n" + 
				"</div>\n" + 
				"<!-- -->\n" + 
				"            </div>\n" + 
				"        </div>\n" + 
				"    </div>\n" + 
				"    <!-- B2C 상품 -->\n" + 
				"<!-- _BLOG_CONTENTS_HEADER_TAIL -->\n" + 
				"    <!-- SE_DOC_HEADER_END -->\n" + 
				"    <div class=\"se-main-container\">\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-b823dc24-4d39-484e-b3b4-6b4d786cbc5c\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7f9b7264-20fa-4388-bafd-4a81513d5602\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-18685391-b9fe-4533-8b8d-9fcbd72b1342\">안녕하세요 </span><span style=\"color:#000000;background-color:#ffd300;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-476e43b2-08f7-4246-aedd-a93d2d110de1\"><b>우드24</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-fae13e40-d767-4bb3-a243-ad55a1353f37\">입니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-58b958ce-8e3b-4fb0-ada0-7d0739ae77ba\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c26fdcb1-6d58-4e57-80d9-7c793c4df90a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fa7af128-01af-4cb9-a3d0-4f4e8d9e13ad\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c20e66da-45f3-4f4c-84a4-16576aa930d2\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-58c1c8b8-cabf-4f07-9f28-009bc7966c76\"><span style=\"color:#000000;background-color:#ffd300;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-a9c3e098-b443-4a65-aa2e-4f0a13f765f9\">우드24</span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-29b8e168-0759-4724-94a3-41ddd258e95e\">를 잠깐 소개하자면요~!</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-700cc1b5-f28d-4669-abaf-ad1cdb29dc61\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-0aed4a66-f9d2-4297-8b69-acf13fb9c946\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-13682ef7-7073-4eb7-baea-4cf1a6fa5b0c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-5b2c46d3-2336-4282-9142-2cd9ecf4cb86\">1. 합성목재를 공장직영으로 판매합니다(국내생산)</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c24d8f35-8f1f-4f05-ae4c-e095a174992e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-181034ed-780d-4852-955f-768b669fa69b\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6f4d5f14-e811-49a8-9480-801633d7e1ca\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-92cff9ea-abed-4bc1-9475-528856b4d14e\">2. 1년 내내 합성목재만을 시공합니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-96e8ab4c-e533-4891-8bb0-0ed09ce6053b\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-e2203b57-587e-4a6d-b3d2-797a611d7798\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3a09be20-1c75-48b3-a30f-9e5d9647cd49\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-a9a4df7b-cacc-43e4-8415-341f730ba71b\">3. 자재 판매를 위한 시공을 하기 때문에</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-75e14cbd-814e-42f9-b7f7-a00f28851e8f\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-5a55a8c3-3585-4413-ade5-7bbc88767081\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-39be1f62-ed46-4769-843b-b4e21a2e8e36\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae   \" id=\"SE-48579c34-ca54-4289-a759-1d0da5c84d06\">    불필요한 시공 마진이 없습니다</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-horizontalLine se-l-default\" id=\"SE-38003e0b-38a5-4e9b-aa9b-c55865618b56\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-horizontalLine se-l-default se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-horizontalLine\">\n" + 
				"                                <hr class=\"se-hr\" />\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-cc065dfc-a25e-42f5-b345-2f849a64b73b\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-21b762d3-a6df-4b30-91bf-8d76d6014150\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f262cabb-63e4-4aa7-9804-125e9f80af60\">이번 시공 현장은 </span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c009cca8-c6b7-4893-9c39-fd9f16e2afcc\"><b>강남 역삼</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e4f478bd-182e-47fb-a202-20ecaf288c98\">인데요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-31d2cf08-ee46-4c4e-8321-8cacfef804d4\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-024cfef1-032e-420c-9522-4831e729f0a4\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5226e7f6-a60d-48a4-9b53-ac3da3e1373c\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ee661ac5-c38a-43df-acfc-8ea34f4671c5\"><b>상가 앞에 아담한 사이즈의</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5baf293b-891d-46a9-a0ce-149d4ef6c0b7\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6a391aab-4ae2-44ae-ab04-dd2221ccf2e2\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-678336cc-1c73-4c78-bbf9-961c75fca371\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-684c2b7e-88d8-4448-95ba-1bdb11d345eb\"><b>데크를 설치</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-b4791132-448a-4411-a4d7-522da83281e2\">한 현장 후기입니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e7b75720-c3da-4279-891d-10d25467f893\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e9f6fede-a0fd-464b-9d23-6d2c4200e7b8\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1f68d9a1-c8bc-4f99-97c0-9f0b580044ef\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-dd1ae21c-573e-40f0-94d8-13c7680dbb47\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-750c7ecd-61ca-46e2-a45f-1aec2ce2d98c\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f0a47537-5661-41c0-9765-af4031cb402e\"><b>데크 자재는 인조 방부목</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-ab6be52f-c543-4865-8bc7-f5dfc8d1000c\">을</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2208972b-daad-4b2b-9a13-db8c5c3bb86a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-27eea5f8-a22c-4c23-93ed-006f2d24b99d\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-86e690fb-1e4e-4b83-a919-807106984f98\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-8674f396-543f-4d26-90ee-0289b109d68b\">사용하였는데//</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-61b83af2-6a5d-48c4-8ffd-44a37ff98ad5\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-1ccd505d-a595-4ed9-b23a-e67183a35816\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5026b881-4801-48a2-bfce-055247290e71\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-da8dcacd-2203-46b0-801a-9a58081a5e6a\"><b>장점이 많아 강추 드리는 자재</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-498debfb-c917-4ec5-b10f-0f9c89e038ff\">이죠^^</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-018b8dde-6667-4ea9-b76b-86096bde5816\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-f592c9a3-6e19-4cee-9f2e-ba34a3dac504\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-sticker se-l-default\" id=\"SE-ba52f847-4c6a-4c6f-80dc-949fdba63315\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-sticker se-section-align-center se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-sticker\">\n" + 
				"                                <a href=\"#\" onclick=\"return false;\" class=\"__se_sticker_link __se_link\" data-linktype=\"sticker\" data-linkdata='{\"src\" : \"https://storep-phinf.pstatic.net/ogq_56a6fe06aef78/original_20.png\", \"packCode\" : \"ogq_56a6fe06aef78\", \"seq\" : \"20\", \"width\" : \"185\", \"height\" : \"160\"}'>\n" + 
				"                                    <img src=\"https://storep-phinf.pstatic.net/ogq_56a6fe06aef78/original_20.png?type=p100_100\" alt=\"\" class=\"se-sticker-image\" />\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-726372f0-bc3f-4221-a6d9-d5907081cff9\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e4bcd481-4864-4e98-8d59-209f5c677205\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5f63d67b-49f6-4c05-8884-520a6ebd25e0\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-641eed64-a39d-46c0-b9a0-b12e7e7c6b99\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-572bafbb-6060-49e2-a81c-b27b8c8a6230\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8572720f-8f81-41e1-8d10-a5ac743956ed\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-cac5e72d-65c8-4ea4-8b8a-40ca7aeb2ae6\"><b>포스팅 순서</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-75e920a1-c3ae-43d3-8342-3a9354bbe9ca\">는 아래와 같습니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3afabe32-4161-4d58-b300-e9febdaf9f84\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-4560e236-2470-4bb2-9e6d-3b8c5864b41a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7de06703-9927-41df-8727-1ee5696b7ffb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-cc8a2e53-9ee3-4da5-a95d-ad88eacb482e\"><b>1. 인조 방부목이란(장단점)</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5154e4a3-c123-4995-8dee-8ef04cfbbbeb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-53b255d5-b1dc-4b59-95dc-72901990b639\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-955faa6e-5603-4773-a178-d51460ac3965\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-7cd05c67-855c-45d5-b1f8-8c243cedb96c\"><b>2. 데크 제작 과정</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-13948caf-bdcc-4c50-a0c1-d55b31f4c09a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-64fb0eaa-c46b-4af4-804e-9d256c278f81\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a15bd400-28e3-4bc9-9884-8bae34314f28\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-375fa483-0169-4d33-9af4-3aecc97530c4\"><b>3. 상가 데크 완성</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d4943ad3-5636-4abf-863f-c7a807ae267f\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-58190d95-1535-4633-9f36-a7a7f906ba39\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c5a94275-595e-4490-9fbd-8bf7e2a8cecd\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-8e92e7bb-c8c0-41ca-8685-0d7e84265eb5\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-29d6476e-cbb0-41c0-a37e-b928b33d7a39\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d67186bb-74bd-4021-8459-c03e3dcc25af\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-quotation se-l-quotation_line\" id=\"SE-69df31f5-e702-4a85-aa97-7553d2e836cc\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-quotation se-l-quotation_line\">\n" + 
				"                            <blockquote class=\"se-quotation-container\">\n" + 
				"                                <div class=\"se-module se-module-text se-quote\"><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-7f672216-f217-446e-94fc-0b90cb6cc381\"><span style=\"color:#000000;\" class=\"se-fs- se-ff-   \" id=\"SE-b6d92c4d-6f20-4c43-9e87-03d269624268\"><b>인조 방부목이란(장단점)</b></span></p><!-- } SE-TEXT --></div>\n" + 
				"                            </blockquote>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-image se-l-default\" id=\"SE-e9a02d18-ef15-45eb-b267-2d41c813ebae\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-e9a02d18-ef15-45eb-b267-2d41c813ebae\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfNCAg/MDAxNjk5OTI1NDQ4ODgx.FpbOmM7NS9gMWQX6HTQ00UH9wXWxn-7lsTrkgGw5Fgkg.65C568OvtxMQeJp2W89lHQnGEHp5wQy1aVS3SsTEZmkg.PNG.wood-24/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9.png\", \"originalWidth\" : \"1000\", \"originalHeight\" : \"984\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfNCAg/MDAxNjk5OTI1NDQ4ODgx.FpbOmM7NS9gMWQX6HTQ00UH9wXWxn-7lsTrkgGw5Fgkg.65C568OvtxMQeJp2W89lHQnGEHp5wQy1aVS3SsTEZmkg.PNG.wood-24/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9.png?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfNCAg/MDAxNjk5OTI1NDQ4ODgx.FpbOmM7NS9gMWQX6HTQ00UH9wXWxn-7lsTrkgGw5Fgkg.65C568OvtxMQeJp2W89lHQnGEHp5wQy1aVS3SsTEZmkg.PNG.wood-24/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9.png?type=w580\" data-width=\"500\" data-height=\"492\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                            <div class=\"se-module se-module-text se-caption\"><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-86a1abdf-984d-4ce1-b761-c5a1d2bd546f\"><span style=\"\" class=\"se-fs- se-ff-   \" id=\"SE-2eff66cd-6dd0-4d44-8366-d3d453033fac\">인조 방부목 데크</span></p></div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-d2351f15-c4ac-4a07-804a-4aa9378f4e26\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-79b6815c-04b3-409c-9d3c-44db01366541\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-a753362e-86c4-49a9-92e4-7d33bd998bda\"><b>인조 방부목</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-62e69b9c-c325-4c80-81cc-c343b11f6242\">은 </span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-67f1378a-b78b-41af-95e9-aff37220e979\"><b>목재 가루와 플라스틱 가루를</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-36d9dc17-c146-4f7a-8ef0-6dfd3531cff6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-dade4dce-3349-4481-8fb8-8990b6c6d4e3\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-64fedcba-b094-4679-be57-b01b6243189c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-a08902a9-a311-4583-b470-cc91d611e70f\"><b>혼합하여 만든 인조 목재</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-51249dfb-c355-48f9-8637-b86636f1e6f1\">인데요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e29dbbca-ad5c-41f3-a647-05685145b3e6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-7d0ad3e1-f247-4a9f-9c06-963512f8353f\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6116449a-3d14-4e3f-85cf-b4dc76a99f57\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-11583b92-0d37-4f7d-9f07-e25cb9e69c39\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-cb1c6ffa-5222-41e3-b52b-29493cc8d511\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-b6f6f432-5aff-467b-bec8-e79246a491aa\"><b>방부목 데크의 단점</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-15ccecc1-02ff-4b18-a48c-f4c77dde659a\">을</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6dbd21a4-f65e-4aa4-9e32-f18e23f89d75\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-8f6c3944-539f-44b2-a690-ad63fa2f1cf8\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5095e2a5-ab1e-477a-923c-7db2de2e974f\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-4348e06d-4648-4521-b256-e4e0e15b8c4c\"><b>보안하여 만든 자재</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ac1ec164-2299-4241-a0de-be6a5a1d8126\">이죠</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1ec28f48-ab67-41b0-9abb-677a03f2adba\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ff7bab76-3979-4774-abd9-7957cbf46472\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a3e90a72-9a2b-4262-b504-56b9d5fb9dff\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e5286562-75a0-49b9-a31a-5b58f2451267\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fc9a00db-58a7-45f5-8730-e49fae31f045\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ad2969a6-3c74-4608-9666-17c005e20303\"><b>방부목의 단점</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-626804c4-e5ce-44ff-a836-f61c797168a6\">이란</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b21495c9-cb9b-4511-8126-3f7c1753da86\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ea97d5eb-b49c-4623-b0c4-437b7d921c5f\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ff6be2a1-9360-4704-bafe-b405b149ae9e\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-241f6dca-77d0-48ae-a4dd-d0183bc00d03\"><b>습기와 충해에 약하고</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fe2104ed-7fc5-411a-8a51-656dd52c0de1\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-2436f055-715e-4091-b883-bc8051e36671\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-691f49e4-8e06-4769-b5db-1a2c64c5d0d5\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-76b619d0-4ef2-42f2-a42b-06ac30930a7d\"><b>꾸준한 오일스테인 관리가</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b5c7a6f7-0230-4745-8cec-135b6d5db102\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c2e912a5-006d-4cd7-afbe-22468a2498c9\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3f1311a1-7cce-42ab-b818-58471f7b244c\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-548bbde6-4da5-4589-92af-c3d462a70713\"><b>필요</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f76b450c-b979-4953-9ff4-c3871e979171\">하다는 점인데요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0942f18b-e261-42df-8341-ae82f6d30593\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-fa251671-cc1c-4fce-85be-c40a182436dd\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-af220b61-0b9d-46dd-98f1-f0c54e3b4971\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-3970612e-8bbd-4f49-84e9-b61882409c0a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-11c11b3c-b6a6-48e1-9497-98e77cab1dec\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c65c8b9b-d438-4a4b-8d16-4570700806a3\">이를 보안한 </span><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-7c9d9316-497f-4a33-87c4-37e66c8ee612\"><b>인조 방부목의 특징</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-40a50fec-f51e-4f65-af51-1d2da5115fa9\">은</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-85dea0c2-518d-447e-b02c-36ba5f30ec7e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-883e811e-24fe-4d2a-be52-2997e7d72a39\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fc641313-5bc3-4808-b9f7-0b6cf8cef89d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-98d4557c-2404-4cd7-8b05-1becfb2e00de\">다음과 같습니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-dd8936a7-d9a5-4f51-a43a-b557a921f679\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-592df526-1110-419e-96b4-83193ed03c09\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b09123a4-9566-4664-93b1-f917edd3e0c2\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ed9844da-5391-4cd0-a639-390f70633fbf\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b0c517b6-593a-4bd3-b5c7-436143014051\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e16b8cc1-0013-4834-9e8f-e37822272f47\"><b>장점</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c5e221ba-aac2-4237-9325-7315bb220962\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-bf82e52d-23f8-42db-9bf5-bcbf4cb0bd6f\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d21a8198-68c8-4cc5-905f-34d1fc991af9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-b894e346-f1ba-4993-b947-7bfa7007f683\"><b>1. 썩지 않아 수명이 긺</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4dbd0709-0af9-4cca-8e1d-7e8165bda1ff\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-65c4c5cb-e921-4227-8a23-8d8ae6036175\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7edaec77-0aa8-4119-8e40-3c23e0f7fa40\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-7b4c1283-a22b-4948-ab9d-6b4378acc562\"><b>2. 오일스테인 관리가 필요 없음</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2845c3f3-5d07-4cd9-b581-6a7921549757\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-4a13f025-51ab-46c2-b7e3-3a1a9c1e564b\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fe341d59-d341-4a7e-af32-728c3e845f1e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-6aab7222-8782-40d6-9b31-88ab75528229\"><b>3. 습기와 충해에 강함</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-42143e7f-2a4f-4e7b-b63a-3da1942dd179\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ee59cc21-a6cb-408b-9225-11801f585095\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-89d55a1e-0d53-4aa8-bb34-c135ca976f99\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-1b0af60e-7a78-498a-af2c-ac9670b653a0\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ebbc572f-d504-4729-8194-3c2c412980df\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-40ce4616-7cc1-4ed2-af23-0389d3eec225\"><b>단점</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b3857873-b67a-47c9-98d9-46725aab1268\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-fae6064b-e3e1-4501-a24e-fc9d776d0f37\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-19e038c9-899d-4ffc-b878-ba2a21180315\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-278443ce-f264-4394-88d5-4446e0eb5704\"><b>1. 방부목에 비해 가격이 비쌈</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e129ac2d-a50f-4883-9214-c5751a0b7f04\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-e69facef-cf83-4a19-a47e-b52c650fa7d8\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8f831800-d66c-4c31-ad4f-353b8218dad9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-52a7597b-5f46-47b2-8f68-312eb38c140a\"><b>2. 통기성이 없어 결로 현상 발생 가능</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1141f51b-048e-4b85-a4f0-f93a0966ae97\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-f402efc1-8e9f-487d-82ff-7fe436e629f7\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-21deb872-9104-442c-92ac-0f2dd2c8e0e1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-d04ef827-20c0-4b6d-8496-77f61c8a4024\"><b>3. 인위적인 느낌</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d87161f7-34a3-4981-ac4c-a253153416eb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-285963d9-87ee-4a10-b3e1-09da53fcb9b4\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-sticker se-l-default\" id=\"SE-f68134e8-a056-4378-ad42-da831ec8d9f3\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-sticker se-section-align-center se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-sticker\">\n" + 
				"                                <a href=\"#\" onclick=\"return false;\" class=\"__se_sticker_link __se_link\" data-linktype=\"sticker\" data-linkdata='{\"src\" : \"https://storep-phinf.pstatic.net/ogq_5dc715e83e7bc/original_13.png\", \"packCode\" : \"ogq_5dc715e83e7bc\", \"seq\" : \"13\", \"width\" : \"185\", \"height\" : \"160\"}'>\n" + 
				"                                    <img src=\"https://storep-phinf.pstatic.net/ogq_5dc715e83e7bc/original_13.png?type=p100_100\" alt=\"\" class=\"se-sticker-image\" />\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-641bc796-8e86-4d07-82a4-2071b932d58e\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ab8db4f3-f0d7-427a-8b5c-3a5ab59cd1b7\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-47c9a855-373e-41f6-b057-a67fb636f3f3\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-28d9f660-b33b-4cc7-b9d8-07512634e58c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e916c690-e6bb-4e4f-a9b4-fd1f511cb0af\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-25199e26-8209-4de8-ae0d-3112ad1fd59c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-174c5c5e-b8ba-4eaf-8e44-3926d596e66c\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-quotation se-l-quotation_line\" id=\"SE-a113711e-50e6-4684-a098-e17a6f35f7aa\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-quotation se-l-quotation_line\">\n" + 
				"                            <blockquote class=\"se-quotation-container\">\n" + 
				"                                <div class=\"se-module se-module-text se-quote\"><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-3be26412-ae42-47e3-b684-3344d4987768\"><span style=\"color:#000000;\" class=\"se-fs- se-ff-   \" id=\"SE-a276faed-b926-4cec-a3fd-69a0f3eda2f3\"><b>데크 제작 과정</b></span></p><!-- } SE-TEXT --></div>\n" + 
				"                            </blockquote>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-imageStrip se-imageStrip2 se-l-default\" id=\"SE-6f19a572-9858-49d7-ad71-cb894628831c\">\n" + 
				"                    <div class=\"se-component-content se-component-content-extend\">\n" + 
				"                        <div class=\"se-section se-section-imageStrip se-l-default\">\n" + 
				"                            <div class=\"se-imageStrip-container se-imageStrip-col-2\">\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"width:50.0875656742557%;\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-9b7e4abc-e655-4956-b0c3-fdc4b88d8161\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMjUx/MDAxNjk5OTI3NzkxMTQ0.rRLX2CjekCViqiNxqs4KTb_Fr_DahelM3WYpIv29jmgg.MMFe39hBNVN4zS3XeF0zQJypEL_n59o1r0QFQvcvZrUg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91.jpg\", \"originalWidth\" : \"3942\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjUx/MDAxNjk5OTI3NzkxMTQ0.rRLX2CjekCViqiNxqs4KTb_Fr_DahelM3WYpIv29jmgg.MMFe39hBNVN4zS3XeF0zQJypEL_n59o1r0QFQvcvZrUg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjUx/MDAxNjk5OTI3NzkxMTQ0.rRLX2CjekCViqiNxqs4KTb_Fr_DahelM3WYpIv29jmgg.MMFe39hBNVN4zS3XeF0zQJypEL_n59o1r0QFQvcvZrUg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91.jpg?type=w275\" data-width=\"500\" data-height=\"285\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"width:49.9124343257443%;\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-80b644f7-13e6-43ce-aea1-d7575cca0e49\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTQ3/MDAxNjk5OTI3OTEwNTM0.Jm9N4gGPuNZxBUqEOX2xAyCApYyeLhipGMy-lYjIsXMg.SF33b5nPFQ29zJg-6ICRGYE6qWa_YWDDRNAdZPojGDgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_3.jpg\", \"originalWidth\" : \"3930\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTQ3/MDAxNjk5OTI3OTEwNTM0.Jm9N4gGPuNZxBUqEOX2xAyCApYyeLhipGMy-lYjIsXMg.SF33b5nPFQ29zJg-6ICRGYE6qWa_YWDDRNAdZPojGDgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_3.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTQ3/MDAxNjk5OTI3OTEwNTM0.Jm9N4gGPuNZxBUqEOX2xAyCApYyeLhipGMy-lYjIsXMg.SF33b5nPFQ29zJg-6ICRGYE6qWa_YWDDRNAdZPojGDgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_3.jpg?type=w275\" data-width=\"500\" data-height=\"286\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-f8aab5e0-d8ad-4413-8bbb-bb7f26c94811\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9701c09d-5fd0-44d0-8d12-5dc6394199ca\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-4d3d4f71-1ba2-455d-a50f-e455008434f8\">사진은 </span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-34e91876-ef32-4da2-aba3-92d41a47e028\"><b>시공 전 현장 모습으로</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-189c8f5a-cdd1-4e40-b15d-a3583fc4ab83\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f195ca27-0c69-4a75-93ac-05886f07e75a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8710dde4-8133-4334-ad3d-6cded80c7a23\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e07e1030-ae49-4e3a-85dc-2f51fb932899\"><b>방부목 데크가 설치</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-0b5d0a4d-7809-4571-997b-82873916c3bd\">돼 있었습니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1f1a231d-5d6d-4855-9366-a6d9ffd88fd8\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6cde7a32-6e69-47b7-a8a6-85d6a3ccb337\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a79d796d-4c47-4a16-85e2-06240d784436\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-2d46b26b-2d1a-4a41-aeb0-36ddea445d1c\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-25e44f49-cd85-4418-a815-a5b8da0836fc\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-30022ef3-21c4-470a-b770-9ad798079074\"><b>상가 데크</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-30799865-5273-4e29-ab98-b01c490cff8d\">의 경우</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-20c8bcbb-af26-4be5-9258-1b69cabd65aa\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-9111e59f-81d2-403e-9d22-4a7d007a13da\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d4dba9dd-666d-4a74-a140-5a9e73019266\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-b11aaa57-f3d4-4b3d-97ab-cdefaddeb601\"><b>방부목은 추천드리지 않는데요</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a9769d96-f128-48e6-8fba-8afae40648d6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-9d3e3ea0-e1c7-4b53-acbc-0de7b8053779\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2b73b179-ca68-4885-8b9d-a5504c574ca2\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c7340d35-6415-48d1-aa25-fa8febbf6f35\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-dd137d65-b26f-489f-9e6a-6fcb0f42618a\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-dd137d65-b26f-489f-9e6a-6fcb0f42618a\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfNTIg/MDAxNjk5OTI5MTUzMjUw.kHJFJtl5zckxdVjED15ZxUUY3rxDz-i-NSl5SwPxS2kg.naKuui-hDOI_TL6Eps-NpDLGZjY7esYx8rij5lnsA8Yg.JPEG.wood-24/SE-dd137d65-b26f-489f-9e6a-6fcb0f42618a.jpg\", \"originalWidth\" : \"3000\", \"originalHeight\" : \"1685\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfNTIg/MDAxNjk5OTI5MTUzMjUw.kHJFJtl5zckxdVjED15ZxUUY3rxDz-i-NSl5SwPxS2kg.naKuui-hDOI_TL6Eps-NpDLGZjY7esYx8rij5lnsA8Yg.JPEG.wood-24/SE-dd137d65-b26f-489f-9e6a-6fcb0f42618a.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfNTIg/MDAxNjk5OTI5MTUzMjUw.kHJFJtl5zckxdVjED15ZxUUY3rxDz-i-NSl5SwPxS2kg.naKuui-hDOI_TL6Eps-NpDLGZjY7esYx8rij5lnsA8Yg.JPEG.wood-24/SE-dd137d65-b26f-489f-9e6a-6fcb0f42618a.jpg?type=w580\" data-width=\"500\" data-height=\"280\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                            <div class=\"se-module se-module-text se-caption\"><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-c7d7d615-0d56-4a2b-8193-527835eb0195\"><span style=\"\" class=\"se-fs- se-ff-   \" id=\"SE-7c369561-029f-4a0d-8d0f-032ee25902c6\">오일스테인 작업 후 데크</span></p></div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-521cbc5a-8a6b-46fc-a086-4b1a98573530\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b64670c9-c644-4f06-9ceb-7b8d3d30dbc9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-9694168e-ea32-47bd-8685-6d3c1e6c9b38\">왜냐하면...</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-080391e4-0c1c-4f33-9e0a-3342162d1d5e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e3442b01-5466-425a-bf3e-16233647ebd4\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b9abda4e-c299-449b-8c69-c19c32d82fa2\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f6762957-8eb8-4d1e-9ef4-33705a599386\"><b>방부목</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-eaf503db-6844-43dd-bb6d-d407a38cd0f7\">은 </span><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-772d0c7a-cac1-4465-89cb-ea7dd202cef5\"><b>매년 오일스테인을</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d0860547-bab2-4e10-a07d-4ac19a963932\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c4f0e576-3cb3-4614-94bb-478d5bdaabc0\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-06f90f1d-ccae-49e4-ba9a-5ed3fb820a96\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-bebb8e32-2413-44fc-b85b-a53ab04a1916\"><b>칠해줘야 수명을 유지</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-52ef73f6-0a35-4d27-962a-aea705de9a8a\">할 수 있는데,</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-56357f60-8a77-487a-99b8-0f0382cfef85\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-20d66f93-a220-41f9-80ec-cf8cab4de59c\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c7e234d8-30fc-46be-868d-b4c48009aa1c\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-11d1ff12-8507-42ed-8f99-d8498c2d16ed\"><b>상가의 경우 영업에 지장</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-dcb6222c-2cdd-42d1-96da-af984808b1fc\">이 있어</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-f1dc107f-c01d-4840-9898-c6de57111675\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-246e1ad1-6288-4d83-8cd6-2558855f6359\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5f2cb3ff-52d2-411d-9301-73efc0885e6f\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-85d98c63-6e9a-4f54-bf4c-918b6f9c6c4c\"><b>오일스테인 작업이 쉽지 않기 때문</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-4e909960-cbff-48e6-8a9e-b110ae211fb3\">이죠</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3dea45c2-cb37-47f8-a6e9-742ef25288d9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-a35054e7-aaca-41e9-9926-dcb87f755d91\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-88df5d8f-bb6b-4582-a091-3507083019f3\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-058edf8e-4016-4265-9a23-37fdcc7a4fb4\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-fdc7ba05-7d94-47fe-a0bf-2ddcfe2e805a\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-fdc7ba05-7d94-47fe-a0bf-2ddcfe2e805a\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTc4/MDAxNjk5OTQ0ODQ3MDQ3.sBAtVumOhMkziu4HbcljxUlZLYaZd_UjgV6G1tO7OV0g.Rfyc5h1AP6Fmtfhape3qA6oaIG-HXw4SVCj93HAnK3wg.JPEG.wood-24/SE-fdc7ba05-7d94-47fe-a0bf-2ddcfe2e805a.jpg\", \"originalWidth\" : \"3000\", \"originalHeight\" : \"1717\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTc4/MDAxNjk5OTQ0ODQ3MDQ3.sBAtVumOhMkziu4HbcljxUlZLYaZd_UjgV6G1tO7OV0g.Rfyc5h1AP6Fmtfhape3qA6oaIG-HXw4SVCj93HAnK3wg.JPEG.wood-24/SE-fdc7ba05-7d94-47fe-a0bf-2ddcfe2e805a.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTc4/MDAxNjk5OTQ0ODQ3MDQ3.sBAtVumOhMkziu4HbcljxUlZLYaZd_UjgV6G1tO7OV0g.Rfyc5h1AP6Fmtfhape3qA6oaIG-HXw4SVCj93HAnK3wg.JPEG.wood-24/SE-fdc7ba05-7d94-47fe-a0bf-2ddcfe2e805a.jpg?type=w580\" data-width=\"500\" data-height=\"286\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-4cdbcd2c-48f8-422a-9ae1-f755824239d2\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-36d2c58b-6b96-40be-bb73-9b9ccbcdfa27\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-240ef813-fb24-4952-bd93-c5d1fd4baadf\">방부목 데크 일부를 뜯어내니</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3d4b1148-74f3-4097-b14c-742099072710\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-5b88b42f-804c-45b4-897b-508712ee2141\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-02ab3755-4656-471b-be14-3a1a95f45789\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-39c176f1-80ab-41d0-b26d-4ee0a81a56c0\"><b>기초하지는 컬러각관</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-37b44059-c181-4057-b0c1-2081b6ada394\">으로</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-961ec042-4732-484e-868e-f6bd8d3e17a9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-459f5cd8-0b53-4599-a9e1-52ee432906ba\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ecd09936-9274-444f-bd39-a8fe21091008\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-39ce86e0-5e9a-49a5-9c16-ecb06cd41ff7\">시공돼 있었는데요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ba73c894-e823-44dd-9cd4-f8f004efe246\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e12446b2-c0c5-4503-a72c-a26658660e86\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ca7e1ed0-751e-4cea-8d87-3f8222aadbb3\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-63c4142c-3cc4-48a6-ad85-5248345459b5\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4ffb37dc-aa03-4be8-863b-fb87c4452f90\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-33c84d7c-be92-44b5-8086-9e8ad1245135\">외부에 사용되는 각관은</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4eb1bf61-ecd3-48ab-ba34-96731cbbd3f2\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-8a4107e6-a91c-4db2-a5d0-e79b2ab9a5f8\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4c3f7500-0fd8-4e28-8551-052d84ed2d2f\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f91722f6-a6cf-4051-9ce0-5c56492f740e\"><b>아연각관을 사용해야 내구성도 좋을뿐더러</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-225aae05-38fa-4304-abf8-6121243923b9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-abda9336-8702-40ee-8efb-556e643fb880\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-be49f531-a2b7-4227-82c7-746037efbef2\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-112e383b-8427-4d5c-aa59-7cd8e3ea47fe\"><b>데크도 제 수명을 사용</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f6e80f77-7f73-4ed9-9cee-dcd20bfb0895\">할 수 있습니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e3649880-fd6e-4aa7-a370-39f42c493d2e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-dacbce8b-0628-4545-80e5-702c361f2175\"><b>(컬러각관은 내부 용도)</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-f43edb9d-c9c3-477f-b9f6-c49c2e3ea7d6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-bf111c72-e9fd-418a-9cf9-83db8d9803ba\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0b07c31e-5408-4f6f-b83e-0f824062b80a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-5fd1e697-a265-4ad0-a3fa-e73c7f829607\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-88f98cd0-d2a9-49df-803c-c10ab7cefb8d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-998b1ffe-9c6a-44ea-83d7-54ab5c55699e\">가격이 저렴하다고</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7af57c9d-9315-4d9b-9f9f-913c5d00dad0\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-f524cbd4-c448-4b4e-8bef-27381e76c349\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fbdea935-061c-41c6-98c7-cb79c072d27b\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-abad2346-b0f6-4b3e-b161-1b9cb7b3e4ac\"><b>컬러각관을 외부 용도로 사용하면</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7f45d860-3b7c-4b27-9044-c106bcb6bbb0\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-e0e67317-f293-47b7-a865-1386f5d6faac\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7a1f8e9d-9b5a-4b65-a59b-dce8e268a84f\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-60af4de5-70fe-42e4-a5ed-29b837765f08\"><b>내구성이 떨어져 데크 수명이</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6d83db15-5566-4984-bfb5-856a3a9332b3\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f63768a7-0b5b-484e-8517-d64dea9a5cea\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b6cdb002-7251-4599-9b83-58360f1b5ebd\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-f4caa9fc-b4e3-430f-90ef-9353f59b08bb\"><b>줄어들 수 있다는 점!</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-55ee86c3-3575-4fbf-a761-8904f5ae7ebd\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-b32c8249-e9e9-4cff-be92-67e24f863564\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-sticker se-l-default\" id=\"SE-cecd936f-d200-43db-be6e-5f158b145996\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-sticker se-section-align-center se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-sticker\">\n" + 
				"                                <a href=\"#\" onclick=\"return false;\" class=\"__se_sticker_link __se_link\" data-linktype=\"sticker\" data-linkdata='{\"src\" : \"https://storep-phinf.pstatic.net/ogq_56a6fe06aef78/original_19.png\", \"packCode\" : \"ogq_56a6fe06aef78\", \"seq\" : \"19\", \"width\" : \"185\", \"height\" : \"160\"}'>\n" + 
				"                                    <img src=\"https://storep-phinf.pstatic.net/ogq_56a6fe06aef78/original_19.png?type=p100_100\" alt=\"\" class=\"se-sticker-image\" />\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-840e5dd8-4bf2-431d-ac87-48a6b7599a64\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7ff5c796-b0e8-47a5-9de2-49a982963887\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-a453a943-58ad-4881-a81c-f8df0d246da3\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-730396d8-bfb8-4968-917c-af035bbf8373\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-dc385efc-b871-4845-89ab-f0e8b4a37ac8\"><b>​</b></span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-imageStrip se-imageStrip2 se-l-default\" id=\"SE-3456c0e4-0f44-49c0-a179-3613c21db145\">\n" + 
				"                    <div class=\"se-component-content se-component-content-extend\">\n" + 
				"                        <div class=\"se-section se-section-imageStrip se-l-default\">\n" + 
				"                            <div class=\"se-imageStrip-container se-imageStrip-col-2\">\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"width:50.0%;\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-a4dc4444-bf2d-4d56-9262-312b39c483a2\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMiAg/MDAxNjk5OTI3OTEwOTgz.A4jZvUAi9EfBJZY2P3zJRSzLAhaRDJu_L4BlIPm-io4g.8SrwJns4Myk0BNuIMIloBnz4wVOmcOOx4DV-ibE3NyIg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_6.jpg\", \"originalWidth\" : \"3972\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMiAg/MDAxNjk5OTI3OTEwOTgz.A4jZvUAi9EfBJZY2P3zJRSzLAhaRDJu_L4BlIPm-io4g.8SrwJns4Myk0BNuIMIloBnz4wVOmcOOx4DV-ibE3NyIg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_6.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMiAg/MDAxNjk5OTI3OTEwOTgz.A4jZvUAi9EfBJZY2P3zJRSzLAhaRDJu_L4BlIPm-io4g.8SrwJns4Myk0BNuIMIloBnz4wVOmcOOx4DV-ibE3NyIg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_6.jpg?type=w275\" data-width=\"500\" data-height=\"283\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"width:50.0%;\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-ce5c74ec-7d0b-4318-b318-be3d541d9aea\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfNDAg/MDAxNjk5OTQ2MzQzNzM2.L5MKuiKAs_hI7Sfi65Y7KnUJ-aaLIKE8uRcXSenkOWAg.qnlckhez1-ngqGIrToSH2m3DH16djFSPmsbUfxxZ6ZIg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_10.jpg\", \"originalWidth\" : \"3972\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfNDAg/MDAxNjk5OTQ2MzQzNzM2.L5MKuiKAs_hI7Sfi65Y7KnUJ-aaLIKE8uRcXSenkOWAg.qnlckhez1-ngqGIrToSH2m3DH16djFSPmsbUfxxZ6ZIg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_10.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfNDAg/MDAxNjk5OTQ2MzQzNzM2.L5MKuiKAs_hI7Sfi65Y7KnUJ-aaLIKE8uRcXSenkOWAg.qnlckhez1-ngqGIrToSH2m3DH16djFSPmsbUfxxZ6ZIg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_10.jpg?type=w275\" data-width=\"500\" data-height=\"283\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-de87e83d-abfa-40dd-bc75-34032cee263a\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-bc26206e-12c3-4011-93d7-94ef42107e12\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-71454702-e81b-43f2-a2f5-c1394fdc01a8\">컬러각관은 재사용이 안되니</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d3765515-1797-486c-bfff-87046d6e38c8\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-0c2fecf1-218d-4126-a95f-abb0c12d7f75\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d084691c-ef92-47c1-8a81-948181519850\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-d7fc3704-63b1-4373-aee5-da71bd95c777\"><b>방부목과 각관 모두 철거</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5d49a0b8-b191-4958-b36f-38be45bc17aa\">해 주었습니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7b901715-1460-481a-b4d3-6d410807f8d4\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-f83f54ed-2a91-4c93-9b2d-a16644fef304\"><b>(아연각관은 재사용 가능)</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5115540a-491d-40bf-a506-9524569d004c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-24ba8e47-059b-4c56-a48d-031e7f77c1e1\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5b5f2860-30be-411b-9ea2-6d011218a2c4\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-ecdcade9-074c-4c31-9450-52b49fa0d605\"><b>​</b></span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-7b3fed79-0416-44fb-85b8-2eddf59ca4c7\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-7b3fed79-0416-44fb-85b8-2eddf59ca4c7\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTM4/MDAxNjk5OTQ2MzQ1MzY5.Uh87K2gBT-tq51j0E600fNNSIf84gaPRskVToUFcmXUg.QY3xGyr4bPhgkfdI-m-4fq1bbJPr72mge7axMcD_hrgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_8.jpg\", \"originalWidth\" : \"3977\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTM4/MDAxNjk5OTQ2MzQ1MzY5.Uh87K2gBT-tq51j0E600fNNSIf84gaPRskVToUFcmXUg.QY3xGyr4bPhgkfdI-m-4fq1bbJPr72mge7axMcD_hrgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_8.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTM4/MDAxNjk5OTQ2MzQ1MzY5.Uh87K2gBT-tq51j0E600fNNSIf84gaPRskVToUFcmXUg.QY3xGyr4bPhgkfdI-m-4fq1bbJPr72mge7axMcD_hrgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_8.jpg?type=w580\" data-width=\"500\" data-height=\"283\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-b0c0949e-cda8-42f0-a242-51a833233e9c\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e4c99978-f969-4a16-842e-5d4ed8604b6c\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-043559ec-025b-4a69-956b-e836937ec582\"><b>기초하지는 당연히 아연각관</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-de56e5db-d095-4209-9bdb-09dc93b3f5b5\">을</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7cdf7f04-db7f-4655-bd12-f060904b4ff1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-3e2aeb29-383f-46b0-9b4a-2f62ed5e10b9\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-bde4be8e-1704-46e0-9a98-3af602f378e5\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-1d00b530-01ac-4fdd-8b4c-31429c697f5c\">사용해 주었구요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2d2f4b6b-5b8b-44a4-a833-0640023b2df1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-2e60b76e-6abc-4bac-9195-6886d73c7c33\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2bab7dcd-72b8-41ea-913d-a7cc3842aeef\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-dd1aa966-d856-488c-8931-942896f9cb03\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-3255b32e-718d-4bea-8385-ff33394be4ac\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-3255b32e-718d-4bea-8385-ff33394be4ac\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMjU2/MDAxNjk5OTYxOTQ2NDIw.PV6t7gnPvXrEyzKywIinasa8VuQdEbLpOSMopWh3elcg.3Z93rSqsyR435UpNzpN3t9wrhJB7Xib3OquzbWACjBcg.JPEG.wood-24/%EC%9D%80%EB%B6%84%EC%8A%A4%ED%94%84%EB%A0%88%EC%9D%B4.jpg\", \"originalWidth\" : \"1467\", \"originalHeight\" : \"500\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjU2/MDAxNjk5OTYxOTQ2NDIw.PV6t7gnPvXrEyzKywIinasa8VuQdEbLpOSMopWh3elcg.3Z93rSqsyR435UpNzpN3t9wrhJB7Xib3OquzbWACjBcg.JPEG.wood-24/%EC%9D%80%EB%B6%84%EC%8A%A4%ED%94%84%EB%A0%88%EC%9D%B4.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjU2/MDAxNjk5OTYxOTQ2NDIw.PV6t7gnPvXrEyzKywIinasa8VuQdEbLpOSMopWh3elcg.3Z93rSqsyR435UpNzpN3t9wrhJB7Xib3OquzbWACjBcg.JPEG.wood-24/%EC%9D%80%EB%B6%84%EC%8A%A4%ED%94%84%EB%A0%88%EC%9D%B4.jpg?type=w580\" data-width=\"500\" data-height=\"170\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-5e177eed-16bc-4940-98e4-9c70e74bfdb3\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7fc8fa26-9825-428d-a11e-777fd4537341\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6a15e240-7bd8-427b-a85f-b20f9b4e825d\"><b>용접을 마친 부위에는</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-772bc83d-cb5a-40cc-972b-a80ebc6c9c49\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-9cf3e94a-c775-49dd-b933-f62a632d6680\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9c75d8eb-61d2-425e-a7e9-c4b084ef475d\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c5829cdc-c2a5-4127-8d33-892d764ab577\"><b>은분 스프레이</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-6072c4a3-2357-48a7-a052-8dfc8f13aed7\">를 뿌려주어야</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-62979df3-a414-4985-bccf-e3e923bb0b33\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-bac7bee6-3b81-4efb-82c9-54445198f363\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6dc08b3c-2873-4ad4-97f4-27dc26ef04f6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-aa2ba8f1-0a31-494e-a1b3-1bc300ffa9f6\"><b>내구성이 더욱 좋아지죠</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1992d610-1afd-4397-9670-b4d56461c0da\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-80261717-45b1-47bd-a2af-715cca47b380\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9ade96b6-0c70-4f8a-9a8a-4fe17c7b921d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-15ee0b56-a650-4797-94e6-b9357189469c\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-823fa1d8-d580-49d7-9902-173f63b33f4f\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-823fa1d8-d580-49d7-9902-173f63b33f4f\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTg4/MDAxNjk5OTQ2MzQ0NzIx.IRHCSMMmQSsZvFrmmhteUrq5S-YCXnhkOg0-zvlrux4g.zDC1Kc5PMGoQe1d5oiaF4tjHndhBwHgFC4uJMh62GY4g.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_7.jpg\", \"originalWidth\" : \"3966\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTg4/MDAxNjk5OTQ2MzQ0NzIx.IRHCSMMmQSsZvFrmmhteUrq5S-YCXnhkOg0-zvlrux4g.zDC1Kc5PMGoQe1d5oiaF4tjHndhBwHgFC4uJMh62GY4g.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_7.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTg4/MDAxNjk5OTQ2MzQ0NzIx.IRHCSMMmQSsZvFrmmhteUrq5S-YCXnhkOg0-zvlrux4g.zDC1Kc5PMGoQe1d5oiaF4tjHndhBwHgFC4uJMh62GY4g.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_7.jpg?type=w580\" data-width=\"500\" data-height=\"283\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-bbe2b725-b06c-4838-af0b-e8efbde9f5a6\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4de6bbca-f9ce-4382-bb90-f8f0f545f7b8\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-dc7928c8-e7d3-4bc1-84eb-01729f65271e\"><b>인조 방부목 하지 작업 포인트!!</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9407e01b-0be0-42e2-a9d1-7436f6c5da27\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-5eec9dcc-f584-4e45-adc4-a4a8d12abba2\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e1079642-f70f-4e05-8bfb-2fc6201719de\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-5b8a9c0c-ab66-4ffc-be8b-a6e8c50c01e8\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e6f07f28-5758-4876-a7d1-83531d55efe2\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-acb9fdbf-4d16-43ab-b202-bacc71c93172\"><b>멍에 간격 - 1m 이하</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a4c41b8b-a8cd-4347-b656-3031af86b82e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-a70adcdb-0ab2-47fe-bf6a-03d9941d599c\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a314510c-12e3-45a8-9d30-8f29f0ad4eeb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-0beed6f9-4191-4e36-a28f-bdb6329f3000\"><b>장선 간격 - 400mm 이하(25T 데크 기준)</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-42580a7b-388c-4216-bd68-2501ce8d6e5a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-0abb1358-eaa0-457e-a715-829793884887\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-21e7fff1-06a3-4dfe-a740-05356ded7888\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-cef25af1-1ad3-40e1-b48d-a9a83f4fafc3\"><b>이중장선 - 데크가 이어지는 지점에는</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-86e99d25-50db-4f62-84dc-bbf2561daa77\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-7f220c17-fedc-45cc-8780-9410f001e3c4\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-30a5c908-7ba5-475f-ac6a-b160eb546d37\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-1011264d-3229-4e8e-862b-1bcd33330fcc\"><b>장선을 이중으로 설치하여,</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-68eb7bbd-5052-492b-8b4c-dd23aa52a330\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-12325675-30fb-4fe5-9316-87ee31277564\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-f8aeebeb-3574-4ef5-a60e-6fecc66813e2\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-84f69227-3644-4f83-a1b3-f6099075bca2\"><b>각관 한 본에 데크 한 장씩을 고정</b></span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-697b954f-524f-4cb7-ab06-8aac341636b4\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-697b954f-524f-4cb7-ab06-8aac341636b4\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTUx/MDAxNjk5OTYyMzY2Nzg3.mNKxBzxLZCNwioKWRFMxwZ8gxvqcbWE1g86_373wPbEg.2J5jgXmOyE6blR987UIDfVO1SEluiYMqWLC3YraMiwgg.PNG.wood-24/image.png\", \"originalWidth\" : \"730\", \"originalHeight\" : \"185\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTUx/MDAxNjk5OTYyMzY2Nzg3.mNKxBzxLZCNwioKWRFMxwZ8gxvqcbWE1g86_373wPbEg.2J5jgXmOyE6blR987UIDfVO1SEluiYMqWLC3YraMiwgg.PNG.wood-24/image.png?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTUx/MDAxNjk5OTYyMzY2Nzg3.mNKxBzxLZCNwioKWRFMxwZ8gxvqcbWE1g86_373wPbEg.2J5jgXmOyE6blR987UIDfVO1SEluiYMqWLC3YraMiwgg.PNG.wood-24/image.png?type=w580\" data-width=\"500\" data-height=\"126\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                            <div class=\"se-module se-module-text se-caption\"><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-fd5d85d7-2a92-4295-ad72-def94fd7b000\"><span style=\"\" class=\"se-fs- se-ff-   \" id=\"SE-c4c41e22-7222-4760-bfd6-37745b9d63a9\">이중장선 설치 예시</span></p></div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-sticker se-l-default\" id=\"SE-eae7a1d0-28e6-4426-948c-74e4c316a151\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-sticker se-section-align-center se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-sticker\">\n" + 
				"                                <a href=\"#\" onclick=\"return false;\" class=\"__se_sticker_link __se_link\" data-linktype=\"sticker\" data-linkdata='{\"src\" : \"https://storep-phinf.pstatic.net/ogq_5746b9b455ca6/original_15.png\", \"packCode\" : \"ogq_5746b9b455ca6\", \"seq\" : \"15\", \"width\" : \"185\", \"height\" : \"160\"}'>\n" + 
				"                                    <img src=\"https://storep-phinf.pstatic.net/ogq_5746b9b455ca6/original_15.png?type=p100_100\" alt=\"\" class=\"se-sticker-image\" />\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-3911c8f7-d9a0-4bcd-9e38-8405d922316e\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8dd42862-52a8-4bdb-adab-05b8d609b3a1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-83a1e60a-ed96-48fb-9fbf-76d61307bfd6\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-422cc0e6-6bba-4ada-a7db-a80ff81d79f7\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-718ee06c-13c7-4814-bd4a-22d0dd741015\">위 내용은 제가 매번 드리는 말씀이라</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0595c566-2bd6-459c-9873-1d93737350db\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d7d2b83c-9b05-42ff-900d-0c6f7cff91ce\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a792c42f-9948-4c34-af53-e13e12f9ecba\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-cafd157f-7b2f-470d-864a-886defc61066\"><b>자세한 내용은 아래 포스팅</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-acaf87f3-6794-45e1-8441-d83b3e283247\">으로</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-dceff204-4277-47a8-a387-7c039f81c371\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-0f7134c9-2502-436e-985c-d17d0638dd70\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8629aa75-4257-4bf9-914f-eeb46e774a0a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-e56e3fb9-abdb-4896-90f8-d2a5014c80f3\">대체할께요!!</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3ae9a730-6e34-4e24-8635-5c85abbccd65\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-74a6005e-b18f-4396-a15d-f4365dd7a459\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-oglink se-l-image\" id=\"SE-9dfba7d8-34ae-49a3-b21c-4d8455268b99\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-oglink se-l-image se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-oglink\">\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/222493936812\" class=\"se-oglink-thumbnail\" target=\"_blank\">\n" + 
				"                                    <img src=\"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzAyMjdfMjgg%2FMDAxNjc3NDYzNzk2NDM1.U2Yb1G2p9nIZofFkYF_akaDwTuVDmgKJey_KV3aPF5Yg.BMGiIx13m9Z968U-3XTcwaM1MkuWasRqMHgcv74o0YIg.JPEG.wood-24%2F%25B5%25A5%25C5%25A9_%25B0%25F8%25BB%25E7_%25B1%25E2%25C3%25CA%25C7%25CF%25C1%25F6.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\" class=\"se-oglink-thumbnail-resource\" alt=\"\" />\n" + 
				"                                </a>\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/222493936812\" class=\"se-oglink-info\" target=\"_blank\">\n" + 
				"                                    <div class=\"se-oglink-info-container\">\n" + 
				"                                        <strong class=\"se-oglink-title\">데크 공사 시 기초하지 명칭 별 용도(with 하지 시공방법)</strong>\n" + 
				"                                        <p class=\"se-oglink-summary\">안녕하세요~ 더 좋은 생활공간을 만드는 우드24입니다. 데크 공사 시 기초하지 명칭 별 용도와 시공 시 주...</p>\n" + 
				"                                        <p class=\"se-oglink-url\">blog.naver.com</p>\n" + 
				"                                    </div>\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_oglink\", \"id\" :\"SE-9dfba7d8-34ae-49a3-b21c-4d8455268b99\", \"data\" : {\"link\" : \"https://blog.naver.com/wood-24/222493936812\", \"isVideo\" : \"false\", \"thumbnail\" : \"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzAyMjdfMjgg%2FMDAxNjc3NDYzNzk2NDM1.U2Yb1G2p9nIZofFkYF_akaDwTuVDmgKJey_KV3aPF5Yg.BMGiIx13m9Z968U-3XTcwaM1MkuWasRqMHgcv74o0YIg.JPEG.wood-24%2F%25B5%25A5%25C5%25A9_%25B0%25F8%25BB%25E7_%25B1%25E2%25C3%25CA%25C7%25CF%25C1%25F6.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\"}}'></script>\n" + 
				"                </div>                <div class=\"se-component se-horizontalLine se-l-default\" id=\"SE-18009829-486e-455b-895e-c4ed2c768640\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-horizontalLine se-l-default se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-horizontalLine\">\n" + 
				"                                <hr class=\"se-hr\" />\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-18f70e67-c94d-494e-8230-ffa4c71adae9\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8a3b4922-6f10-47b9-9fd7-0084a23c0472\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-4890e548-52df-44c1-aaf2-6371263f6b8a\">하지 작업 후에는</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-32a926be-75a9-4cb2-a0f8-2d6a58a06548\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-30d7df17-6e63-4525-a096-60ccefcbd91e\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d15c3f6c-367c-4ec2-b3dd-dd4e5adff952\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-b3d96581-476b-4444-8954-436d719f7546\"><b>데크 상판재를 설치</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-b0aa9141-c818-4582-80e9-30a51e3f8cfb\">해 줍니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-05496f09-4ad2-44b7-84e7-980975575c55\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-c66910dc-0eeb-442a-9b58-becdeab725fa\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-628e4590-26d0-4268-a22a-52f9cd894278\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5a9ec5a5-7d4f-4a72-ab3d-9ca279dd495f\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9e857df6-a5b1-4034-a942-76a46df15343\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-7d54d251-e43a-48c5-8a37-745256a1a51c\"><b>데크는 인조 방부목을 사용</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-aa74a520-be30-4fe3-bd4e-e00128c0f4de\">하였다고</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-f71f61a5-f9e8-4365-87c0-6c8ee00e2b95\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-be42accd-7f84-4694-9c39-84daa9ae9c80\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-606fd1a8-36dd-43b6-9e99-bdd03960682f\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-2e8b543e-ba06-4891-b7ea-d96050d2ae21\">말씀드렸죠??</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7ac4889f-cfa6-432e-8cb6-078568504518\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-432dfa38-a0fc-47a4-b245-714780d0465a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-962c9171-ee7f-4f0b-bfad-778f2ddc1563\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-52121e79-594c-46e3-a0ff-270147b7a135\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8bd8f255-cbc1-4b37-adee-bd8da8893931\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-1fb4777f-4605-4441-bf50-764e8f064b4e\"><b>인조 방부목 데크 자재 선택하는 요령</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5b296d95-5118-4213-9891-de38c58e3a93\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-cc4eb4bb-d362-4939-a4ad-f136d9e5f1a6\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6fbade0a-41e1-42b9-b736-0889541ef7bf\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d8a5432a-edd0-4013-b9db-03a1397f598f\">몇 가지 알려드릴께요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e957c9d6-5456-449a-a8de-8aaff787edc0\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-8a2e59df-ac68-4b89-8df2-5214c3d39994\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b14bcf2a-b54b-43ca-a682-9b630c692a6d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-ef9c632d-100d-4560-abe3-ba0cb1966361\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3c50779a-e642-4585-8aac-1bf336c14451\"><span style=\"color:#0095e9;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-64874e78-419c-49e3-939a-2933e62eda8f\"><b>첫 번째. 솔리드와 중공형 중</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-044f0a94-0f6e-4ebb-a22c-5363c0b4f552\"><span style=\"color:#0095e9;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-d7799945-73cc-4b18-af87-5d11ddcbdb9b\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4653b084-e8c9-46a2-80fc-439b43bd1b72\"><span style=\"color:#0095e9;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ac7b404f-83a5-4be7-9c19-b20cff45bc3d\"><b>솔리드 제품을 선택한다</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6bb3b11f-e7dd-47ff-88d8-0ca9bf5e73a6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-ffe55c0d-0c20-41df-8d9b-fe3c33c143b5\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-566882f9-0df8-4526-8e16-47b5a9a219b9\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-566882f9-0df8-4526-8e16-47b5a9a219b9\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMjQ2/MDAxNjk5OTYzMTExMTk4.VjVMwHqRjCPdWzYvICwCLLIGKSUsD6O2Yn5FKtG24J4g.UHQ0VvewL9LFJT3rLevFdoDACdSANt054di0ESyDS4Eg.JPEG.wood-24/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9_2.jpg\", \"originalWidth\" : \"800\", \"originalHeight\" : \"383\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjQ2/MDAxNjk5OTYzMTExMTk4.VjVMwHqRjCPdWzYvICwCLLIGKSUsD6O2Yn5FKtG24J4g.UHQ0VvewL9LFJT3rLevFdoDACdSANt054di0ESyDS4Eg.JPEG.wood-24/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9_2.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjQ2/MDAxNjk5OTYzMTExMTk4.VjVMwHqRjCPdWzYvICwCLLIGKSUsD6O2Yn5FKtG24J4g.UHQ0VvewL9LFJT3rLevFdoDACdSANt054di0ESyDS4Eg.JPEG.wood-24/%EC%9D%B8%EC%A1%B0%EB%B0%A9%EB%B6%80%EB%AA%A9_2.jpg?type=w580\" data-width=\"500\" data-height=\"239\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-4c1a49d8-244a-4774-9bd8-177f18a5ab5d\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c0414784-72c8-4801-b41b-0b8fab67baa6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-3d892879-a06a-418f-b3af-c09566f3f54f\">위 사진을 보시면 한 번에</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-89444b76-57bc-490a-827e-b0122c26c6ee\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-9034d814-db4b-49c4-af21-9e2cef9a6b2a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-aeb6b6f6-f034-4f05-906f-7cb239da0144\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-f4243b99-f1ea-4dc1-abe3-ff817f11fb10\">이해되시겠죠??</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d6ea35c8-b7f1-444e-9bda-13a5e6d09252\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-2dd6da92-3fbe-4523-b402-2c1216463365\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7113c72f-454b-400b-a108-6aa1affcffc3\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-96c0697b-5f0b-482f-85e3-1fcfb3d46dae\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-585c86b4-1c74-45de-b95d-c962e14504b7\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-1f05f8f2-2e4e-4811-9ecb-559116f2a96c\"><b>중공형 - 담장이나 가림막 용도</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-68e6231a-acb7-41a9-b416-8223fc9b0247\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-4b06d17b-12bf-4709-b989-7e79b2dde165\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0f227f47-5e29-462f-97f0-8d84c1ae0ae3\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-714e876b-2eb1-4140-929d-5ba7ef05ae5e\"><b>솔리드 - 바닥데크 용도</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-11ffe32d-91d7-40fe-8187-df6384bfab45\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-0fc5a251-35f9-4ecb-a57c-684af2fcfe96\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d949fa9d-cfae-4a26-99b3-8e01be43bee9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-19060559-e318-4443-8897-47c6c2fe9d08\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-33f34fe3-0f44-4671-b8cb-64266be24bbe\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-9e7063ab-d483-47bf-bf56-c9ba645e6c66\">가격이 저렴하다고 </span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-dbade22c-0e56-4864-b78f-842ecf67597c\">​</span><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-811e4407-c388-46ac-8be2-586b0092b203\"><b>바닥데크로</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5152557f-ca42-41c5-9b71-682d01c25e5a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-7f48400e-de04-4ac9-8166-b375b3cd72b1\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-aa74dcf3-e954-4b55-9884-c68e5efb5e3e\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-3a9b08f6-76d4-467d-92d6-9d654bd2dba6\"><b>속이 비어있는 중공형 제품을 사용</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-7bb72a95-df84-490e-9c91-810ef2aaa53b\">한다면</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7c9bb0f5-6f45-4d29-bd24-5bced31d7d10\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-96b8b382-435e-4555-b910-f9abf0a02d1a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4531a7a2-8379-4490-97cb-5ade1676eb6b\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ccc62d49-ae00-44f9-b53d-d23b17aa955a\"><b>하자로 가는 지름길이라는</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-f71b15f5-01e3-4c0e-987b-08ac64529015\"> 점!</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2f2239ce-246e-4e4f-aef4-7d8a86c922c5\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-b084549b-335b-4dff-a371-2e0c371043e1\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7f52ebb7-d0d1-4e67-bc47-d9df18d95443\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5c983892-71ad-49e7-b5ac-9aaa9d2d1990\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b43bf02d-c5aa-41fc-aeec-4dc50f953b1d\"><span style=\"color:#0095e9;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-33c7add8-f17c-45e7-a5ce-d1371dc115b6\"><b>두 번째. 국내산 제품 이용 권장</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-35c302b1-92f6-4378-85be-ce86314feb57\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-14f1272d-5438-4c0a-9619-bddfa855824a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c581f043-9a69-4bfa-9858-e7b5294a8feb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d4c0de33-657f-4d87-aefd-1fad8cfd7669\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-707267a1-ebab-4348-9428-ef20efd400f3\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-4c1c788a-c1f9-4907-a1db-6e717250047d\"><b>인조 방부목</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-021e93be-37b4-4620-b10b-4bf74093292a\">은 대부분 </span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6475c8ef-0f58-4e53-b03d-f27de4c8209e\"><b>국내산과</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2cf00b9c-9909-488b-a9cd-a05863639d21\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-8ff3fc83-331f-4004-81e8-d9a74bb38f65\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5e09c720-b074-4d03-9c2f-2e7def8b3428\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-158a3c13-a7a6-434b-b59e-1092de35dc2f\"><b>중국산 제품</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-e4127ef5-85db-44b9-a156-a1a2bc7fa67b\">으로 나누어지는데요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3195caba-7887-46a4-b8bb-23c56c2821b8\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-6441bb3f-d3db-44ce-a7bc-55309666c156\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-aeb357b3-8b82-4384-bfd9-19def3433f0d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-2dbd1e9f-c437-4fec-8f64-9df60225b05a\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e3952f52-0612-40ad-aff8-8226a7368fb0\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-e2fc7103-44fc-41b7-971f-38dc75694b42\">중국산이라고 품질면에서 많이</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-07fd9730-f1c8-428f-ac71-bcad6eb70762\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-01255213-bef5-4f94-ae38-c7060fa97695\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1ea2792e-bd1c-451c-8ebd-a5fd8b7275b1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-e92c2aa0-d69c-46be-a56c-4dc64ab8f2a7\">떨어지는 것은 아니지만</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0ec9c867-39c4-4110-9933-8d833d6264ac\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-df0aea29-9505-47d9-b8cd-228812094879\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2af61c59-6852-48f4-9741-e3ce29301d1a\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-366396e3-2ae8-4091-972a-6f2644c03fd3\"><b>대부분 유통 업체에서 판매되기 때문에</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-fbc304ef-bfe6-47df-b15b-176848b6d114\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6187706b-218f-40c3-ba08-d4d40348c6b5\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8c19af3d-3d2d-40b3-bcb8-a3fe03b818b8\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-ee367dc6-a26f-4d61-91c3-f85f85185f20\"><b> 추후 as가 어려울 수 있습니다</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6d370d41-e2eb-49da-ac7b-faead8a0d663\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-b9c38b7b-03b2-43f9-993c-9bc38d177fc1\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1af5a73f-1b8d-41a0-b9b0-310777223bd4\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-12e14c3d-025b-4832-8555-7ec5d7ff4c04\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ab138d38-c01c-498f-9e11-5b9710076df8\"><span style=\"color:#0095e9;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-d22ee81b-7509-43c3-9027-9ec46d4864c7\"><b>세 번째. 시험성적서 확인</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-22c225dc-59b6-4823-bb79-67045c5d9a69\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-13516ca9-e4b4-4a5a-8fcd-c812e06e3c20\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d509b121-be4d-4df5-82e9-28187b0172f1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d34df3fa-7a2b-434b-a443-f568d222021b\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-6d7a59d1-bf55-4e3f-b754-7d2e5c0cd3a8\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-1a56aadb-8e32-458a-9706-153fd839e66d\">시험성적서가 있다는 것은</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0ef56885-f0b6-41b2-a9a9-530e17449c71\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d027bfac-fcef-40ce-83a5-5f5c4e4402d0\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c04c36c1-5332-4fe0-a46d-9c41e8175c10\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-16467d42-5a9c-4ad4-91af-78d0bc583d76\"><b>제품이 공인된 시험 기관에서</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-4899c95d-3d40-47d4-a7aa-df468e07e7ac\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-91d869b8-d2ba-457f-acb8-26489c5f4b20\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1519f0ab-3207-4fa3-aaaf-9db7a2331d5d\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-52273889-0db5-4fb8-8f6c-8abaab672755\"><b>시험을 통과하고 인정받았다는</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a97756b2-7606-4b8e-8967-a00f9171010c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-7e410b34-618f-4bc8-a908-5aa1658ee2df\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d96a0ff6-a813-43e1-b388-d7d65edbd877\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-c4e946bc-bfaa-44e9-aeae-4514fbc82c9d\">뜻이니 안심하고 사용하셔도 됩니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-42d8856f-5ad2-4661-a188-65ed269fa526\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-fba97c0c-d435-4098-adda-720f1a68fb2b\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-sticker se-l-default\" id=\"SE-e3268c79-7178-43ab-8c4a-526820a64cfd\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-sticker se-section-align-center se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-sticker\">\n" + 
				"                                <a href=\"#\" onclick=\"return false;\" class=\"__se_sticker_link __se_link\" data-linktype=\"sticker\" data-linkdata='{\"src\" : \"https://storep-phinf.pstatic.net/ogq_5746b9b455ca6/original_19.png\", \"packCode\" : \"ogq_5746b9b455ca6\", \"seq\" : \"19\", \"width\" : \"185\", \"height\" : \"160\"}'>\n" + 
				"                                    <img src=\"https://storep-phinf.pstatic.net/ogq_5746b9b455ca6/original_19.png?type=p100_100\" alt=\"\" class=\"se-sticker-image\" />\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-911f0579-6537-4a2b-942c-e7e59d86c58e\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8ae67916-c8ea-4e53-9445-79b611fec386\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-172644bd-d442-4fbe-a24f-58aaf3d64dd0\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-619f61e9-3d37-4262-8399-9521fdcaa110\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-87fa0100-a2aa-4876-ae7d-cd09d97efd5f\">위 조건을 모두 갖춘 </span><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-895c96ce-2056-4415-a275-393d808f281b\"><b>우드24제품</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-0b527c04-d647-4398-ad53-f648e8a4fbff\">을</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-0bbb01ee-9a84-4dd3-850e-2e0df9dae554\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-e7064369-8a7d-43c2-a14b-d5b7f17cf255\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-bae82b9c-eb5c-4b7e-aa62-494af954f854\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-e3167d23-b3e9-4115-982f-6bc09e3b2f7f\">아래 링크에 남겨드리니 참고해 보시구요^^</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-material se-l-default\" id=\"SE-b837ae7e-72b8-4e99-b4a5-c7f804beadab\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-material se-section-align-center se-l-default\">\n" + 
				"                            <a href=\"https://smartstore.naver.com/main/products/5063837576\" target=\"_blank\" class=\"se-module se-module-material se-material-shopping __se_link \" data-linktype=\"material\" data-linkdata='{\"id\" : \"SE-b837ae7e-72b8-4e99-b4a5-c7f804beadab\", \"type\" : \"shopping\", \"title\" : \"25T 합성 인조 방부목 데크\", \"link\" : \"https://smartstore.naver.com/main/products/5063837576\", \"dataId\" : \"82608359124\", \"thumbnail\" : \"https://shopping-phinf.pstatic.net/main_8260835/82608359124.5.jpg\" }'>\n" + 
				"                                    <div class=\"se-material-thumbnail\">\n" + 
				"                                        <img class=\"se-material-thumbnail-resource\" src=\"https://shopping-phinf.pstatic.net/main_8260835/82608359124.5.jpg\" alt=\"\"/>\n" + 
				"                                    </div>\n" + 
				"                                <div class=\"se-material-info\">\n" + 
				"                                    <div class=\"se-material-info-container\">\n" + 
				"                                        <strong class=\"se-material-title\">25T 합성 인조 방부목 데크 </strong>\n" + 
				"                                        <dl class=\"se-material-detail\">\n" + 
				"                                        </dl>\n" + 
				"                                    </div>\n" + 
				"                                </div>\n" + 
				"                            </a>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_material_shopping\", \"id\" :\"SE-b837ae7e-72b8-4e99-b4a5-c7f804beadab\" , \"data\" : { \"materialId\" : \"82608359124\", \"subId\" : \"\", \"shoppingInfo\" : {\"mallName\":\"우드24\",\"nPayPcType\":\"2\",\"price\":\"19,800\",\"isMatchSale\":false,\"nPayMblType\":\"2\"} }}'></script>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-1bf29083-fee9-45bd-8d42-56abddd06a99\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-34e81fe0-f8d7-4d8e-90ea-b72b98155bd4\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-cb7cbc22-dbf4-41d1-bc7d-f4becaf4a97f\"> </span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c0d8cc9e-4473-437c-b0f3-1c67336947a7\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-7fa57cd2-b4f4-41df-91cc-cdc59657371f\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-113cf030-64ec-45dc-8215-9923401fd377\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-ccb36fd1-55f0-4ef9-a68d-3d4321e314a2\">글을 적다 보니 너무 길어져</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-abb80fe1-a104-480f-a2b4-56bdef035300\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5b5769ac-cfce-4e32-839c-2f5199cc7d9c\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-706c5a21-ce78-4cb6-b4eb-0d9420dc54fc\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-42d9f514-5e02-427f-9f85-5fa02c6f6d9b\"><b>상판재 시공 방법</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-092debb6-4845-48fa-b6ec-220a86c44c1d\">은 아래 포스팅으로</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-f6560105-4afa-4300-a2e0-ff5241cd44fb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-8a252cf3-c313-44ba-89ba-b2cd445ddbef\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-cc707a15-6b59-423d-88e2-84cae6e0f91c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5839d6df-e568-4a8c-91a6-ceb163046679\">대체하겠습니다~</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-69ce66a2-05e9-4d8c-9fe3-09e88973e5c2\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-3917853d-8ab4-441e-9a72-077a69e75bd7\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-oglink se-l-image\" id=\"SE-7b616d91-40d0-41f6-97e7-cd8eae0373a3\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-oglink se-l-image se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-oglink\">\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/221403936510\" class=\"se-oglink-thumbnail\" target=\"_blank\">\n" + 
				"                                    <img src=\"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzAyMjdfMTQ2%2FMDAxNjc3NDY0MTMzMzEy._C4sdpiC9KWcwNOLca_QhGjaKY1wvNttitNO1rNZQQUg.WE0XWnIp9hOkYY9Z-llnJjIbdaAJBmo0I5hv5jOgr1Ag.JPEG.wood-24%2F%25BF%25EC%25B5%25E524.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\" class=\"se-oglink-thumbnail-resource\" alt=\"\" />\n" + 
				"                                </a>\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/221403936510\" class=\"se-oglink-info\" target=\"_blank\">\n" + 
				"                                    <div class=\"se-oglink-info-container\">\n" + 
				"                                        <strong class=\"se-oglink-title\">이것만 보면 하자없다 우드24 합성목재 데크 시방서</strong>\n" + 
				"                                        <p class=\"se-oglink-summary\">안녕하세요~ 더 좋은 생활공간을 만드는 우드24입니다. 우드24 합성목재 데크 시방서 먼저 시방서란 공사의...</p>\n" + 
				"                                        <p class=\"se-oglink-url\">blog.naver.com</p>\n" + 
				"                                    </div>\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_oglink\", \"id\" :\"SE-7b616d91-40d0-41f6-97e7-cd8eae0373a3\", \"data\" : {\"link\" : \"https://blog.naver.com/wood-24/221403936510\", \"isVideo\" : \"false\", \"thumbnail\" : \"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzAyMjdfMTQ2%2FMDAxNjc3NDY0MTMzMzEy._C4sdpiC9KWcwNOLca_QhGjaKY1wvNttitNO1rNZQQUg.WE0XWnIp9hOkYY9Z-llnJjIbdaAJBmo0I5hv5jOgr1Ag.JPEG.wood-24%2F%25BF%25EC%25B5%25E524.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\"}}'></script>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-fc4408da-6dee-4832-8ee4-0b345336dff4\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-8d4ce39e-51d7-4530-9d75-f1aa76afe38c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-84e78a4c-c4e4-48cd-a244-9c3c7c78f2f0\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9333ec9c-0a38-477a-bdd5-0bcaf188bc3e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-16c0d3b6-ee8e-44db-bea3-eb738f112136\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-163236f9-d4c7-4df6-b2c0-ed90b756b36e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-6e44890a-db3e-4eb0-8e2c-fcc88e3b66cf\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-quotation se-l-quotation_line\" id=\"SE-3e673044-8f88-4605-94b4-ed8d557c79a6\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-quotation se-l-quotation_line\">\n" + 
				"                            <blockquote class=\"se-quotation-container\">\n" + 
				"                                <div class=\"se-module se-module-text se-quote\"><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-2e4df841-a06f-4cb5-b625-cc4c1826bef8\"><span style=\"color:#000000;\" class=\"se-fs- se-ff-   \" id=\"SE-5f30f9b2-eb5d-4d1c-9fbb-648e72d457d4\"><b>상가 데크 완성</b></span></p><!-- } SE-TEXT --></div>\n" + 
				"                            </blockquote>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-image se-l-default\" id=\"SE-72697d6b-b380-490c-9246-00fec9eea18b\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-72697d6b-b380-490c-9246-00fec9eea18b\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMzEg/MDAxNjk5OTY2OTM4NTE3.qKe82Qq00wUS9sprLLEi_zi4yxs46RyYxjn9UvW5JOUg.pXv7x86wkbXZhjZNx-jlalx5LHZoSuc3fDoATHUbDfQg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_13.jpg\", \"originalWidth\" : \"3587\", \"originalHeight\" : \"2072\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMzEg/MDAxNjk5OTY2OTM4NTE3.qKe82Qq00wUS9sprLLEi_zi4yxs46RyYxjn9UvW5JOUg.pXv7x86wkbXZhjZNx-jlalx5LHZoSuc3fDoATHUbDfQg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_13.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMzEg/MDAxNjk5OTY2OTM4NTE3.qKe82Qq00wUS9sprLLEi_zi4yxs46RyYxjn9UvW5JOUg.pXv7x86wkbXZhjZNx-jlalx5LHZoSuc3fDoATHUbDfQg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_13.jpg?type=w580\" data-width=\"500\" data-height=\"288\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-image se-l-default\" id=\"SE-4f3ad6a5-c7b0-4f50-8029-448026d2050a\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-4f3ad6a5-c7b0-4f50-8029-448026d2050a\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMzMg/MDAxNjk5OTY2OTMwNzgw.1xu_Yg0hcMWDyf203AcZZgOUIylKefWUyQaTquJO070g.-Kwy66x1J5A3NBjhCQ20gTHFZJrM4rM19Nhiz_SWNhYg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_19.jpg\", \"originalWidth\" : \"3981\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMzMg/MDAxNjk5OTY2OTMwNzgw.1xu_Yg0hcMWDyf203AcZZgOUIylKefWUyQaTquJO070g.-Kwy66x1J5A3NBjhCQ20gTHFZJrM4rM19Nhiz_SWNhYg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_19.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMzMg/MDAxNjk5OTY2OTMwNzgw.1xu_Yg0hcMWDyf203AcZZgOUIylKefWUyQaTquJO070g.-Kwy66x1J5A3NBjhCQ20gTHFZJrM4rM19Nhiz_SWNhYg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_19.jpg?type=w580\" data-width=\"500\" data-height=\"282\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-cc57afa0-1ada-42e8-bbac-01e39f19d26f\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-78b508c1-8a05-4116-9bce-0ea6b8e69947\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-58e1c6f2-27a2-47b8-a969-e404d1357815\">시공 끝!!</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d9306a22-b959-43fa-8922-9d076725e968\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-04c055c1-41c9-4b12-bb3c-986cf11ce080\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b9754ba7-736f-44bd-8633-e2503b73717c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-20df522f-46c6-48f6-bf20-42fbb90b11b0\"><b>아담한 상가 데크 완성!</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-633bcd27-0680-44cd-b289-525506cb54be\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-0d471508-fe18-469e-880f-d1202ceeed1e\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e443a6bf-67f1-4ea1-9adb-fa0b10c6d771\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-5b1dd71b-f927-412e-90de-ab256517d325\">깔끔하죠??</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-64af3270-0dc9-4181-95f6-06773a8de089\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-b437c170-6ead-4188-9c31-4484e6a6560c\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ff20de9f-e6cb-4260-b4ca-39a983ee4868\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-62d941c1-8f77-47d1-9dbb-3b95e023daa2\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-79007fab-8a1f-48a5-a2f5-da7c9b11d1f5\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-79007fab-8a1f-48a5-a2f5-da7c9b11d1f5\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTA2/MDAxNjk5OTY3MzkwMTkz.TCITRJ3-vzeX8dBf-EhGy5b6t4YniVfadpw2MeLUmSIg.dJvUStE86ZRrNJgEiZUFC3wPY_iDiKRKiiTxrl04XXog.JPEG.wood-24/SE-79007fab-8a1f-48a5-a2f5-da7c9b11d1f5.jpg\", \"originalWidth\" : \"3000\", \"originalHeight\" : \"1696\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTA2/MDAxNjk5OTY3MzkwMTkz.TCITRJ3-vzeX8dBf-EhGy5b6t4YniVfadpw2MeLUmSIg.dJvUStE86ZRrNJgEiZUFC3wPY_iDiKRKiiTxrl04XXog.JPEG.wood-24/SE-79007fab-8a1f-48a5-a2f5-da7c9b11d1f5.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTA2/MDAxNjk5OTY3MzkwMTkz.TCITRJ3-vzeX8dBf-EhGy5b6t4YniVfadpw2MeLUmSIg.dJvUStE86ZRrNJgEiZUFC3wPY_iDiKRKiiTxrl04XXog.JPEG.wood-24/SE-79007fab-8a1f-48a5-a2f5-da7c9b11d1f5.jpg?type=w580\" data-width=\"500\" data-height=\"282\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-7f53faf6-ba28-45bb-9a1a-cd99be41722c\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-d0ba5439-e4cb-48dc-96b1-a383761db816\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-8bbc656a-0b71-4060-8b53-3dc018c2bd7e\"><b>데크 가장 자리의 마감 철판은</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-3ad60414-ecd5-4933-89b0-23ffbb5d264d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-ef756211-85de-4a07-8a2c-277b50e7fb3c\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b6cc8861-3547-470a-bc2a-1a9f0e2a59ac\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-b74765ec-7cfe-482a-9ce7-627ab7e0aac2\"><b>기존에 있던 것은 재사용</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-8f5cc454-3ae7-44a9-99d1-746812fc26a2\">하여</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-52973f1d-745d-4253-94ec-a4f6a3a61c14\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-4e93cfa8-a370-4337-80e6-84c1ea73b03e\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e7ef4ff0-83a0-4c8f-b0e6-d7aae028711a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-95690154-17de-4b9a-8b89-6a0288da6d7f\">좀 지저분하긴 한데요</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-50892506-4cf5-4927-a8b6-70338b3dbd2e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-acb5d850-ed48-4ec4-b429-86a18a8ac263\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a0025346-1819-4204-8ca7-836e5548bf53\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-2bd73b20-4a10-4fd2-9c9a-3a9e535bab0e\"> </span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-53e4937c-ae85-4353-b820-a033038cae7b\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-fb2ac5bd-9c2f-4be5-9a1f-3ee6929cac97\">제 생각에는 같은 데크로</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-bfd97607-815f-46c2-a997-9ebe7d3b2050\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-b3fe43a9-66f2-41f6-82e5-8569e49e44da\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5db63927-5794-4f38-9845-4a07955902bc\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-3aae4c27-735d-42fc-803f-5e776ca77692\">마감하는 것이 통일감 있고</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-107a6284-aeb8-4ee2-ad18-befb62127aff\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-e2faa321-dc55-4893-9053-a68795231614\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-78e0c4d3-8648-4969-af6b-25bc008cd51a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-555e3087-63e4-4c05-a5a0-4a529a6e22ed\">더욱 잘 어울렸을 것 같다는</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e41ce70e-22d0-4df5-80ed-30e0ed60b1c6\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-1158b0cc-ccce-4cdd-a0a2-508cc0922121\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-bf7757a5-13ef-4e14-810b-b1fac69bce48\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-246d18d0-41a6-45de-af38-bf9a9957f1b0\">아쉬움이 있었습니다...</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-be299d14-441a-469d-872a-b49ad4b24c6e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-145fe613-4146-4558-8408-1b6f48c0a315\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-264fa23c-369d-49c6-8786-76fa41657a36\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-66ae3634-ae52-4863-a6da-256a54689072\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-12381bd7-4a71-4eff-bd9c-95d320d32174\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-12381bd7-4a71-4eff-bd9c-95d320d32174\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTM5/MDAxNjk5OTY2OTM3ODM2.KlNoUaaQYJRxAvGWnfu5wCHPryhomL2M4CkmNIBxRZAg.r2l30OKmNHz5HvYFMfWNSUbQM-SOlmLQM8j7Qe6bovgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg\", \"originalWidth\" : \"3912\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTM5/MDAxNjk5OTY2OTM3ODM2.KlNoUaaQYJRxAvGWnfu5wCHPryhomL2M4CkmNIBxRZAg.r2l30OKmNHz5HvYFMfWNSUbQM-SOlmLQM8j7Qe6bovgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTM5/MDAxNjk5OTY2OTM3ODM2.KlNoUaaQYJRxAvGWnfu5wCHPryhomL2M4CkmNIBxRZAg.r2l30OKmNHz5HvYFMfWNSUbQM-SOlmLQM8j7Qe6bovgg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_12.jpg?type=w580\" data-width=\"500\" data-height=\"287\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-image se-l-default\" id=\"SE-40f90f32-2e18-47fa-8c13-eb593e2c8d49\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-40f90f32-2e18-47fa-8c13-eb593e2c8d49\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMjY0/MDAxNjk5OTY2OTM1MzYz.fgdGx3lFjO6-MSZeeV3pI2nOjoO8rDREAC9Ag6c6FK0g.IdaXlBamSC5w5N2f64sBCqs3pI3xr-xbx7-uz4-ET-cg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_17.jpg\", \"originalWidth\" : \"3944\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjY0/MDAxNjk5OTY2OTM1MzYz.fgdGx3lFjO6-MSZeeV3pI2nOjoO8rDREAC9Ag6c6FK0g.IdaXlBamSC5w5N2f64sBCqs3pI3xr-xbx7-uz4-ET-cg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_17.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjY0/MDAxNjk5OTY2OTM1MzYz.fgdGx3lFjO6-MSZeeV3pI2nOjoO8rDREAC9Ag6c6FK0g.IdaXlBamSC5w5N2f64sBCqs3pI3xr-xbx7-uz4-ET-cg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_17.jpg?type=w580\" data-width=\"500\" data-height=\"285\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-3938a380-cfed-400c-aa9e-28d7c0eafdf9\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-89fbf363-adce-4bc6-b372-3b77408dc47d\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c7efac92-6213-4167-90e1-1c914d5dfdf4\"><b>인조 방부목</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-eee2a753-47ee-4921-bc46-6001e42aac68\">이라 하여도</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-eba3e2a7-9a5b-48fa-8dee-d836f63b317a\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-1002a4a4-339a-492f-8aad-66e8ce3a25ca\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-9f3e2ff3-7784-4dfc-a900-647bc466bf40\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-5a7ab5a3-61ef-44bd-a836-c1476de9321d\"><b>리얼 목재와는 거의 구분이</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-dd620b1c-6e12-472b-b099-2597a61c18d1\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-c0d45ab9-5da5-4ada-b921-1e306c090e78\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-88ce9acf-8071-4178-b29d-b80ed9425dfc\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-1bf3be78-2cc5-4e53-8e80-da1b0932f78d\"><b>어려울 정도로 비슷</b></span><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-3e2ccfab-750a-4ce8-81d5-87eab53a3755\">하다는 점!!</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ad9ca275-90e7-4a7a-939d-41360b770b97\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-1d54380b-5b13-4ad6-ab1f-de0478297c2a\">(저도 자세히 보지 않으면</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c55fc666-cc13-4a1f-aa6b-ed1994d03ef3\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumdasisijaghae se-weight-unset  \" id=\"SE-8b993cac-a836-47e0-9014-34eb66eea8c5\">구분이 좀 힘들 때가 있어요..)</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-06c005c2-a3fc-4df0-92f7-fcfce9f3807e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-a011121a-144c-4b54-85f7-26ebd87929ad\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b7f833a9-7764-4bbc-97b0-db4e4f1d4823\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-65cfefc2-5617-48e9-9999-6214a48f2580\">​</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-imageStrip se-imageStrip2 se-l-default\" id=\"SE-d9ed309b-2723-4cee-b399-b4281ec3b4b5\">\n" + 
				"                    <div class=\"se-component-content se-component-content-extend\">\n" + 
				"                        <div class=\"se-section se-section-imageStrip se-l-default\">\n" + 
				"                            <div class=\"se-imageStrip-container se-imageStrip-col-2\">\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"width:50.43630017452006%;\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-97bfc17c-62ac-4d65-a30a-6f023dbfaa7c\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMjcw/MDAxNjk5OTY2OTk2OTIx.DkTQoxjqaH6rtkLReTO7AoPIeYt21YcZp05AX4sRSosg.PlQhGglISRAy9AXwZ68DzRz7E_n9ZbJRBpgSUhBKCnEg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_16.jpg\", \"originalWidth\" : \"3954\", \"originalHeight\" : \"2252\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjcw/MDAxNjk5OTY2OTk2OTIx.DkTQoxjqaH6rtkLReTO7AoPIeYt21YcZp05AX4sRSosg.PlQhGglISRAy9AXwZ68DzRz7E_n9ZbJRBpgSUhBKCnEg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_16.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjcw/MDAxNjk5OTY2OTk2OTIx.DkTQoxjqaH6rtkLReTO7AoPIeYt21YcZp05AX4sRSosg.PlQhGglISRAy9AXwZ68DzRz7E_n9ZbJRBpgSUhBKCnEg.JPEG.wood-24/%EA%B0%95%EB%82%A8_%EC%83%81%EA%B0%80%EB%8D%B0%ED%81%AC_%EC%A0%9C%EC%9E%91_16.jpg?type=w275\" data-width=\"500\" data-height=\"284\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"width:49.56369982547993%;\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-a13cf5b8-a076-4f11-8cf8-0b974cb9abf5\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMjk4/MDAxNjk5OTY3MTUwODQw.qcXsxLVn1D7oPz5JlfRqLFNcoDv5wD9OkeVXNXZ3q2wg.afy7WUlDzms1GGlzU9RIS59MW_q3vfNNB0vtCGZxPEog.JPEG.wood-24/SE-a13cf5b8-a076-4f11-8cf8-0b974cb9abf5.jpg\", \"originalWidth\" : \"3000\", \"originalHeight\" : \"1734\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjk4/MDAxNjk5OTY3MTUwODQw.qcXsxLVn1D7oPz5JlfRqLFNcoDv5wD9OkeVXNXZ3q2wg.afy7WUlDzms1GGlzU9RIS59MW_q3vfNNB0vtCGZxPEog.JPEG.wood-24/SE-a13cf5b8-a076-4f11-8cf8-0b974cb9abf5.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMjk4/MDAxNjk5OTY3MTUwODQw.qcXsxLVn1D7oPz5JlfRqLFNcoDv5wD9OkeVXNXZ3q2wg.afy7WUlDzms1GGlzU9RIS59MW_q3vfNNB0vtCGZxPEog.JPEG.wood-24/SE-a13cf5b8-a076-4f11-8cf8-0b974cb9abf5.jpg?type=w275\" data-width=\"3000\" data-height=\"1734\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-5cef201c-d1d4-41d0-9e3f-e9113b5c937d\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-46560cdd-b2ca-4da9-b1ef-46ff6bb54faa\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-4ae89613-5d89-4765-9d1b-920ecbb4fdfa\">위 사진을 보시면</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-00e58b5b-1952-4421-8b7f-7c0ca169dbd9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-849ad8c3-8c89-42a4-8d97-d9479c4c1f35\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-bdbe994f-2ed0-4117-88e7-4b8791811a94\"><span style=\"color:#000000;background-color:#ffe3c8;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-395dcf41-c83b-40a2-9e0e-75dd2c5c7677\"><b>꼼꼼함이 팍팍 느껴지시죠??</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e3a9ac6b-0134-4f5d-bb0b-37ac20dd92e4\"><span style=\"color:#ff0010;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-03dfe1ed-4211-4c57-8484-93d82af10d2f\"><b>​</b></span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-quotation se-l-default\" id=\"SE-c6f3900d-2363-414e-aff4-6d4ddded7cdf\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-quotation se-l-default\">\n" + 
				"                            <blockquote class=\"se-quotation-container\">\n" + 
				"                                <div class=\"se-module se-module-text se-quote\"><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-545078b8-e633-4e57-8435-ad3f6ea3bca6\"><span style=\"\" class=\"se-fs- se-ff-   \" id=\"SE-cbf8a528-6275-4892-a09e-53fda35a435f\">디테일에 강한 것이</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-2ce3fa1b-32f8-4bfb-a201-175c46ef1004\"><span style=\"\" class=\"se-fs- se-ff-   \" id=\"SE-b91fb90c-7f4f-4ede-ae64-deaa27d232c1\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align- \" style=\"\" id=\"SE-d6215bd3-4a02-40db-abb7-aa43aa977694\"><span style=\"\" class=\"se-fs- se-ff-   \" id=\"SE-0e330d0d-9bb9-4473-9c31-81c3585ee67c\">바로 시공 능력의 차이이죠</span></p><!-- } SE-TEXT --></div>\n" + 
				"                            </blockquote>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"                <div class=\"se-component se-text se-l-default\" id=\"SE-d5e0ff08-31b7-4973-a3df-080be4e36fc3\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-a41742b2-a865-4986-aabf-df9e51e3f7a3\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d014759b-18e6-4ce1-aa4c-1dc56c9cdb5d\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2b1964e9-4e9f-4aef-bab9-57b266b1e86e\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-fa99370a-4a7d-4e7b-bfbe-2a3914bbd723\">이런 꼼꼼한 시공..</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-acafeb75-c245-4bb2-994b-33a7681fc45c\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-cfc37ad4-3a44-4ccf-a9d2-cd25767bbab6\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-621f7645-117d-4b5f-9d6d-c21e8a781b53\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d9edb254-781c-4397-86a0-18826214d883\">바로 우드24가 해내고 있습니다^^</span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-sticker se-l-default\" id=\"SE-69dbcc53-d43a-45ea-acf0-e4fa121bf4f4\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-sticker se-section-align-center se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-sticker\">\n" + 
				"                                <a href=\"#\" onclick=\"return false;\" class=\"__se_sticker_link __se_link\" data-linktype=\"sticker\" data-linkdata='{\"src\" : \"https://storep-phinf.pstatic.net/ogq_5dc715e83e7bc/original_21.png\", \"packCode\" : \"ogq_5dc715e83e7bc\", \"seq\" : \"21\", \"width\" : \"185\", \"height\" : \"160\"}'>\n" + 
				"                                    <img src=\"https://storep-phinf.pstatic.net/ogq_5dc715e83e7bc/original_21.png?type=p100_100\" alt=\"\" class=\"se-sticker-image\" />\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-text se-l-default\" id=\"SE-ab301c98-accf-43fd-a9ca-f309218e97d4\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-text se-l-default\">\n" + 
				"                            <div class=\"se-module se-module-text\">\n" + 
				"                                    <!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ff07f104-80da-47f5-b446-c493bcbfe7f4\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-695f572e-6a82-4c1e-9a79-db10fd720983\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-e5f35d18-9c15-40dd-876c-dbe2ac587b47\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6ebe22d1-f143-4eb0-b412-648997bffc5f\"><b>- 결론 - </b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7821c821-3031-4004-9765-e6a498ccd6ff\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-b39ef503-ce1e-4d60-ac6b-b9cea8fca69b\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-c383b66f-ba4c-442c-a23f-df699a9a867d\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-af92f6f4-34a4-485f-8756-41adb4d76f8a\"><b>상가데크는 인조 방부목을 사용해야</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-7a183bbd-1029-4279-8f9f-2d374bb993eb\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-a6d6d0cb-1282-4d33-acec-4c93a99e2cb9\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-632ec5ea-0f26-4beb-bdaf-719c45976786\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-a4046ed1-1087-4957-8796-f1256a2e0603\"><b>영업에 지장을 받지 않고</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-5c6c0727-562c-488d-bf9b-9db9e093bd04\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-af670f79-52b4-469f-be4b-9fffb595eb51\"><b>​</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-2461f696-8657-4e16-8fa3-5c6cff2eefc8\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-nanumbareunhipi   \" id=\"SE-2feb55c1-1308-40e4-86cd-2f9ae9fc46ec\"><b>오래 사용할 수 있다!!</b></span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-b24ee99c-d8ee-46d2-8528-f6451c5d7fa9\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-82cc0b88-0f50-4ead-a54f-80798902b6da\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-da009520-76cb-4b1d-82ad-abc783407b77\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-405a5c38-750b-4997-9ef7-f5b8d5165114\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-1f0c80d9-3ce2-4b44-a1f9-f0a30b40c621\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-d36c5f4d-66b1-4c18-8dbd-f1457c3cec6e\">이상 포스팅을 마치겠습니다</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-ee0eff40-669a-4c6b-9ba7-d52284dd4842\"><span style=\"color:#000000;\" class=\"se-fs-fs16 se-ff-system se-weight-unset  \" id=\"SE-1fa2e0a6-be91-43cd-9394-8c1742bb308f\">​</span></p><!-- } SE-TEXT --><!-- SE-TEXT { --><p class=\"se-text-paragraph se-text-paragraph-align-center \" style=\"line-height:2.1;\" id=\"SE-21a2b352-58ac-4a33-8fff-3fd648a1c839\"><span style=\"color:#000000;background-color:#ffef34;\" class=\"se-fs-fs16 se-ff-system   \" id=\"SE-6c534d5c-f401-4d9e-8db8-19a3a5601aa9\"><b>함께 보시면 좋은 글</b></span></p><!-- } SE-TEXT -->\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>                <div class=\"se-component se-oglink se-l-image\" id=\"SE-f179ead5-1ac9-4ebe-9f60-796b6ba3629c\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-oglink se-l-image se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-oglink\">\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/223135670406\" class=\"se-oglink-thumbnail\" target=\"_blank\">\n" + 
				"                                    <img src=\"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzA2MjJfMTEx%2FMDAxNjg3Mzk5OTE3OTE4.Dh8RUcu4s39RiZEFdadPMbGttgefYdWbHgc8KJivt04g.uk3w83tx2vn_TCz_yifREPW_cCBNVNUWm8XwFHB4olog.JPEG.wood-24%2F%25C7%25D5%25BC%25BA%25B9%25E6%25BA%25CE%25B8%25F1_%25B5%25A5%25C5%25A9_5.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\" class=\"se-oglink-thumbnail-resource\" alt=\"\" />\n" + 
				"                                </a>\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/223135670406\" class=\"se-oglink-info\" target=\"_blank\">\n" + 
				"                                    <div class=\"se-oglink-info-container\">\n" + 
				"                                        <strong class=\"se-oglink-title\">반드시 알아야 할 합성방부목 데크 시공 팁!!(가격 공개)</strong>\n" + 
				"                                        <p class=\"se-oglink-summary\">안녕하세요 우드24입니다. 이번 현장은 분당인데요 상가 테라스에 데크를 설치한 후기입니다. 자재는 합성...</p>\n" + 
				"                                        <p class=\"se-oglink-url\">blog.naver.com</p>\n" + 
				"                                    </div>\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_oglink\", \"id\" :\"SE-f179ead5-1ac9-4ebe-9f60-796b6ba3629c\", \"data\" : {\"link\" : \"https://blog.naver.com/wood-24/223135670406\", \"isVideo\" : \"false\", \"thumbnail\" : \"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzA2MjJfMTEx%2FMDAxNjg3Mzk5OTE3OTE4.Dh8RUcu4s39RiZEFdadPMbGttgefYdWbHgc8KJivt04g.uk3w83tx2vn_TCz_yifREPW_cCBNVNUWm8XwFHB4olog.JPEG.wood-24%2F%25C7%25D5%25BC%25BA%25B9%25E6%25BA%25CE%25B8%25F1_%25B5%25A5%25C5%25A9_5.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\"}}'></script>\n" + 
				"                </div>                <div class=\"se-component se-oglink se-l-image\" id=\"SE-4a34f550-6d47-4d3b-9d13-d995cc1a5685\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-oglink se-l-image se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-oglink\">\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/223160488128\" class=\"se-oglink-thumbnail\" target=\"_blank\">\n" + 
				"                                    <img src=\"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzA3MTlfMjUg%2FMDAxNjg5NzQyNTkzNTg1.nqYYcnK4Bgiy5qcfwEOT9npfHA3H5qpFxfpSPSkC8jUg.mnwwoaAxLoe8BMZMxFdwCaXii4vSH_LiLi2B6RDFDdsg.JPEG.wood-24%2F%25C4%25AB%25C6%25E4_%25C5%25D7%25B6%25F3%25BD%25BA_%25C7%25D5%25BC%25BA%25B5%25A5%25C5%25A9_%25BD%25C3%25B0%25F8_16.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\" class=\"se-oglink-thumbnail-resource\" alt=\"\" />\n" + 
				"                                </a>\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/223160488128\" class=\"se-oglink-info\" target=\"_blank\">\n" + 
				"                                    <div class=\"se-oglink-info-container\">\n" + 
				"                                        <strong class=\"se-oglink-title\">방부목 철거 후 합성데크 시공(상가)</strong>\n" + 
				"                                        <p class=\"se-oglink-summary\">안녕하세요 우드24입니다 이번 현장은 송파인데요 상가 테라스 방부목 데크가 낡아 철거를 하고, 합성데크...</p>\n" + 
				"                                        <p class=\"se-oglink-url\">blog.naver.com</p>\n" + 
				"                                    </div>\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_oglink\", \"id\" :\"SE-4a34f550-6d47-4d3b-9d13-d995cc1a5685\", \"data\" : {\"link\" : \"https://blog.naver.com/wood-24/223160488128\", \"isVideo\" : \"false\", \"thumbnail\" : \"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzA3MTlfMjUg%2FMDAxNjg5NzQyNTkzNTg1.nqYYcnK4Bgiy5qcfwEOT9npfHA3H5qpFxfpSPSkC8jUg.mnwwoaAxLoe8BMZMxFdwCaXii4vSH_LiLi2B6RDFDdsg.JPEG.wood-24%2F%25C4%25AB%25C6%25E4_%25C5%25D7%25B6%25F3%25BD%25BA_%25C7%25D5%25BC%25BA%25B5%25A5%25C5%25A9_%25BD%25C3%25B0%25F8_16.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\"}}'></script>\n" + 
				"                </div>                <div class=\"se-component se-oglink se-l-image\" id=\"SE-185a90c5-1833-407d-8443-3434cb37e780\">\n" + 
				"                    <div class=\"se-component-content\">\n" + 
				"                        <div class=\"se-section se-section-oglink se-l-image se-section-align-center\">\n" + 
				"                            <div class=\"se-module se-module-oglink\">\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/223153038694\" class=\"se-oglink-thumbnail\" target=\"_blank\">\n" + 
				"                                    <img src=\"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzA3MTFfMjcy%2FMDAxNjg5MDU4MjU3ODY4.lsHKjUuCq47H6Xhi7_SDadTN8vGUB9IQJKgH4jv_eswg.JmJJNAEqiPgRLKvYq4u5AFc4SrkDbPXKEHvvKCcvNgwg.JPEG.wood-24%2F%25C0%25CE%25C1%25B6%25B5%25A5%25C5%25A9_%25C6%25FA%25B8%25AE%25C4%25AB%25BA%25B8%25B3%25D7%25C0%25CC%25C6%25AE_%25C1%25F6%25BA%25D8_%25B8%25B6%25B4%25E7_%25B2%25D9%25B9%25CC%25B1%25E2_11.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\" class=\"se-oglink-thumbnail-resource\" alt=\"\" />\n" + 
				"                                </a>\n" + 
				"                                <a href=\"https://blog.naver.com/wood-24/223153038694\" class=\"se-oglink-info\" target=\"_blank\">\n" + 
				"                                    <div class=\"se-oglink-info-container\">\n" + 
				"                                        <strong class=\"se-oglink-title\">인조데크 x 폴리카보네이트 지붕 (마당 꾸미기)</strong>\n" + 
				"                                        <p class=\"se-oglink-summary\">안녕하세요 우드24입니다 이번 시공 현장은 경기도 이천이구요 전원주택 테라스에 데크와 데크 지붕을 설치...</p>\n" + 
				"                                        <p class=\"se-oglink-url\">blog.naver.com</p>\n" + 
				"                                    </div>\n" + 
				"                                </a>\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_oglink\", \"id\" :\"SE-185a90c5-1833-407d-8443-3434cb37e780\", \"data\" : {\"link\" : \"https://blog.naver.com/wood-24/223153038694\", \"isVideo\" : \"false\", \"thumbnail\" : \"https://dthumb-phinf.pstatic.net/?src&#x3D;%22https%3A%2F%2Fblogthumb.pstatic.net%2FMjAyMzA3MTFfMjcy%2FMDAxNjg5MDU4MjU3ODY4.lsHKjUuCq47H6Xhi7_SDadTN8vGUB9IQJKgH4jv_eswg.JmJJNAEqiPgRLKvYq4u5AFc4SrkDbPXKEHvvKCcvNgwg.JPEG.wood-24%2F%25C0%25CE%25C1%25B6%25B5%25A5%25C5%25A9_%25C6%25FA%25B8%25AE%25C4%25AB%25BA%25B8%25B3%25D7%25C0%25CC%25C6%25AE_%25C1%25F6%25BA%25D8_%25B8%25B6%25B4%25E7_%25B2%25D9%25B9%25CC%25B1%25E2_11.jpg%3Ftype%3Dw2%22&amp;type&#x3D;ff120\"}}'></script>\n" + 
				"                </div>                <div class=\"se-component se-video se-l-default\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-video se-section-align-center se-l-default\" >\n" + 
				"                            <div class=\"se-module se-module-video\" id=\"SE-4150c831-b328-4bac-9a99-78145322aafe\">\n" + 
				"                            </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                    <script type=\"text/data\" class=\"__se_module_data\" data-module='{\"type\":\"v2_video\", \"id\" :\"SE-4150c831-b328-4bac-9a99-78145322aafe\", \"data\" : { \"videoType\" : \"player\", \"vid\" : \"26B7CF336CFD6A71510B294117F8B8263260\", \"inkey\" : \"V12569fdf09721279322b1f75c640e79b43ebbb19b623cf62165badc1560dcdf47e8d1f75c640e79b43eb\", \"thumbnail\": \"https://phinf.pstatic.net/image.nmv/blog_2023_11_14_182/0dcf9b31-82f2-11ee-a9b6-80615f0bcd26_08.jpg\", \"originalWidth\": \"854\", \"originalHeight\": \"480\", \"width\": \"500\", \"height\": \"281\", \"contentMode\": \"fit\", \"format\": \"normal\", \"mediaMeta\": {\"@ctype\":\"mediaMeta\",\"title\":\"인조방부목 상가데크 제작\",\"tags\":[\"인조방부목\",\"상가데크\",\"데크제작\"],\"description\":\"인조방부목 상가데크 제작\"}, \"useServiceData\": \"false\"}}'></script>\n" + 
				"                </div>                <div class=\"se-component se-image se-l-default\" id=\"SE-39384997-809f-4c19-b6b7-692e000518d4\">\n" + 
				"                    <div class=\"se-component-content se-component-content-fit\">\n" + 
				"                        <div class=\"se-section se-section-image se-l-default se-section-align-center\" >\n" + 
				"                                <div class=\"se-module se-module-image\" style=\"\">\n" + 
				"                                    <a href=\"#\" class=\"se-module-image-link __se_image_link __se_link\" style=\"\" onclick=\"return false;\" data-linktype=\"img\" data-linkdata='{\"id\" : \"SE-39384997-809f-4c19-b6b7-692e000518d4\", \"src\" : \"https://postfiles.pstatic.net/MjAyMzExMTRfMTAx/MDAxNjk5OTY4Nzc5MzYz.Z9zF4FxlC9d_diBEtnrK93m1m6o8vsfz1uOUY3eESWAg.W03MEsZiZsMvmrQp2dRjf48Vnnfxkjq-nvvyXRTzR7Ig.JPEG.wood-24/SE-39384997-809f-4c19-b6b7-692e000518d4.jpg\", \"originalWidth\" : \"400\", \"originalHeight\" : \"230\", \"linkUse\" : \"false\", \"link\" : \"\"}'>\n" + 
				"                                        <img src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTAx/MDAxNjk5OTY4Nzc5MzYz.Z9zF4FxlC9d_diBEtnrK93m1m6o8vsfz1uOUY3eESWAg.W03MEsZiZsMvmrQp2dRjf48Vnnfxkjq-nvvyXRTzR7Ig.JPEG.wood-24/SE-39384997-809f-4c19-b6b7-692e000518d4.jpg?type=w80_blur\" data-lazy-src=\"https://postfiles.pstatic.net/MjAyMzExMTRfMTAx/MDAxNjk5OTY4Nzc5MzYz.Z9zF4FxlC9d_diBEtnrK93m1m6o8vsfz1uOUY3eESWAg.W03MEsZiZsMvmrQp2dRjf48Vnnfxkjq-nvvyXRTzR7Ig.JPEG.wood-24/SE-39384997-809f-4c19-b6b7-692e000518d4.jpg?type=w580\" data-width=\"500\" data-height=\"287\" alt=\"\" class=\"se-image-resource\" />\n" + 
				"                                    </a>\n" + 
				"                                </div>\n" + 
				"                        </div>\n" + 
				"                    </div>\n" + 
				"                </div>\n" + 
				"    </div>\n" + 
				"</div>\n" + 
				"						</div>\n" + 
				"						\n" + 
				"\n" + 
				"						\n" + 
				"						\n" + 
				"						\n" + 
				"\n" + 
				"						\n" + 
				"\n" + 
				"					\n" + 
				"					\n" + 
				"					\n" + 
				"\n" + 
				"					\n" + 
				"						\n" + 
				"						<div id=\"post_footer_contents\" class=\"post_footer_contents\">\n" + 
				"							\n" + 
				"							\n" + 
				"							\n" + 
				"							\n" + 
				"							<div class=\"wrap_tag\">\n" + 
				"								\n" + 
				"									\n" + 
				"									\n" + 
				"										\n" + 
				"											\n" + 
				"											\n" + 
				"												<div class=\"_param(false|false|4)\" id=\"tagList_223264915610\" style=\"display:none;cursor:default;\">\n" + 
				"													<strong class=\"blind\">태그</strong>\n" + 
				"												</div>\n" + 
				"											\n" + 
				"										\n" + 
				"									\n" + 
				"								\n" + 
				"								\n" + 
				"								<div style=\"display:none\">\n" + 
				"									<span class=\"wrap_input_text\">\n" + 
				"										<input type=\"text\" class=\"input_text pcol2 _tagInputArea\" title=\"태그를 입력하세요.\" value=\"\">\n" + 
				"										<i class=\"aline\"></i><i class=\"pcol2b\"></i>\n" + 
				"									</span>\n" + 
				"									<span class=\"wrap_btn\"><a href=\"#\" class=\"btn pcol2 _cancelTag _returnFalse _param(223264915610)\">취소<i class=\"aline\"></i></a>\n" + 
				"									<a href=\"#\" class=\"btn pcol2 _saveTag _rosRestrict _returnFalse _param(223264915610)\">확인<i class=\"aline\"></i></a></span>\n" + 
				"								</div>\n" + 
				"								\n" + 
				"							</div>\n" + 
				"						</div>\n" + 
				"\n" + 
				"						<div class=\"post-btn post_btn2\">\n" + 
				"                            <div class=\"wrap_postcomment\">\n" + 
				"                                \n" + 
				"								<div id=\"area_sympathy223264915610\" class=\"area_sympathy pcol2\">\n" + 
				"									<div class=\"u_likeit_list_module _reactionModule\" data-sid=\"BLOG\" data-did=\"BLOG\" data-cid=\"wood-24_223264915610\" data-catgid=\"post\">\n" + 
				"										<a href=\"#\" role=\"button\" class=\"u_likeit_list_btn _button pcol2 _param(223264915610)\" data-type=\"like\" data-log=\"lik.llike|lik.lunlike\" data-isHiddenCount=\"true\">\n" + 
				"											<span class=\"u_ico _icon pcol3\"></span>\n" + 
				"											<span class=\"u_cnt _count\"></span>\n" + 
				"										</a>\n" + 
				"									</div>\n" + 
				"									<a id=\"Sympathy223264915610\" href=\"#\" role=\"button\" class=\"btn_arr pcol2 _symList _param(3|1|223264915610|0) _returnFalse\">\n" + 
				"										<div class=\"u_likeit_list_module _reactionModule\" data-sid=\"BLOG\" data-did=\"BLOG\" data-cid=\"wood-24_223264915610\" data-catgid=\"post\">\n" + 
				"											<span class=\"u_likeit_list_btn _button btn_sympathy\" data-type=\"like\">\n" + 
				"												<em class=\"u_txt _label\">공감</em>\n" + 
				"												<em class=\"u_cnt _count\"></em>\n" + 
				"											</span>\n" + 
				"										</div>\n" + 
				"										<i class=\"aline bar\"></i>\n" + 
				"										<i class=\"bu_arr\"></i>\n" + 
				"										<span class=\"blind\">이 글에 공감한 블로거 열고 닫기</span>\n" + 
				"									</a>\n" + 
				"                                    <i class=\"aline\"></i>\n" + 
				"								</div>\n" + 
				"                                \n" + 
				"\n" + 
				"                                \n" + 
				"                                <div class=\"area_comment pcol2\">\n" + 
				"                                    <a href=\"#\" role=\"button\" id=\"Comi223264915610\" class=\"btn_comment pcol2 _cmtList _param(1|1|223264915610|0|true) _returnFalse\">\n" + 
				"                                        <i class=\"ico\"></i> 댓글\n" + 
				"										<em id=\"commentCount\" class=\"_commentCount\">\n" + 
				"											17\n" + 
				"										</em>\n" + 
				"										<em id=\"comment_zero_label\">\n" + 
				"											\n" + 
				"										</em>\n" + 
				"										\n" + 
				"											<i class=\"ico_new\"><span class=\"icon_new pcol3\">새 댓글</span></i>\n" + 
				"										\n" + 
				"										<i class=\"aline bar\"></i>\n" + 
				"                                        <span class=\"btn_arr\"><i class=\"bu_arr\"></i><span class=\"blind\">이 글에 댓글 단 블로거 열고 닫기</span></span>\n" + 
				"                                    </a>\n" + 
				"                                    <i class=\"aline\"></i><i class=\"aline3\"></i>\n" + 
				"                                </div>\n" + 
				"                                \n" + 
				"                            </div>\n" + 
				"                        <div class=\"wrap_postedit\">\n" + 
				"                                <!-- 소셜플러그인 -->\n" + 
				"							\n" + 
				"								<div id=\"spiLayer1\" class=\"naver-splugin\"\n" + 
				"									 data-style=\"standard-v1-basic\"\n" + 
				"									 data-url=\"https://blog.naver.com/wood-24/223264915610\"\n" + 
				"									 data-oninitialize=\"splugin_oninitialize(1);\"\n" + 
				"									 data-me-display=\"off\"\n" + 
				"									 data-likeServiceId=\"BLOG\"\n" + 
				"									 data-likeContentsId=\"wood-24_223264915610\"\n" + 
				"									 data-canonical-url=\"https://blog.naver.com/wood-24/223264915610\"\n" + 
				"									 data-logDomain=\"https://proxy.blog.naver.com/spi/v1/api/shareLog\"\n" + 
				"								></div>\n" + 
				"							\n" + 
				"							\n" + 
				"\n" + 
				"							<!-- //소셜플러그인 -->\n" + 
				"\n" + 
				"                                <div class=\"area_btn_postedit pcol2\">\n" + 
				"                                    <i class=\"aline\"></i>\n" + 
				"									\n" + 
				"										<a href=\"#\" class=\"pcol2 _printPost _returnFalse _param(223264915610)\" target=\"_blank\">인쇄\n" + 
				"											\n" + 
				"										</a>\n" + 
				"									\n" + 
				"									\n" + 
				"									\n" + 
				"									\n" + 
				"									\n" + 
				"                                </div>\n" + 
				"                            </div>\n" + 
				"						</div>\n" + 
				"						\n" + 
				"						<div id=\"naverComment_201_223264915610_ct\" style=\"display:none\">\n" + 
				"							\n" + 
				"							<div class=\"commentbox_header _naverCommentHeader\">\n" + 
				"								<a href=\"#\" class=\"btn_write_comment _naverCommentWriteBtn _param(naverComment_201_223264915610|naverComment_201_223264915610_ct) _returnFalse\" title=\"새 창\" >댓글쓰기</a>\n" + 
				"								<div class=\"commentbox_pagination\">\n" + 
				"									<div class=\"num\"><strong class=\"_currentPageNo\">1</strong>/<span class=\"_lastPageNo\">1</span></div>\n" + 
				"									<div class=\"btn_pagination\">\n" + 
				"										<a href=\"#\" class=\"prev dimmed _naverCommentPrev _param(naverComment_201_223264915610_ct) _returnFalse\">\n" + 
				"											<span class=\"blind\">이전</span>\n" + 
				"											<span class=\"icon icon_ic_arrow_8x12_left\"></span>\n" + 
				"										</a>\n" + 
				"										<a href=\"#\" class=\"next _naverCommentNext _param(naverComment_201_223264915610_ct) _returnFalse\">\n" + 
				"											<span class=\"blind\">다음</span>\n" + 
				"											<span class=\"icon icon_ic_arrow_8x12_right\"></span>\n" + 
				"										</a>\n" + 
				"									</div>\n" + 
				"								</div>\n" + 
				"							</div>\n" + 
				"							<div id=\"naverComment_201_223264915610\"></div>\n" + 
				"						</div>\n" + 
				"						\n" + 
				"						\n" + 
				"						<iframe id=\"sympathyFrm223264915610\" allowtransparency=\"true\" src=\"\" width=\"100%\" height=\"1\" style=\"display:none\" scrolling=\"no\" frameborder=\"0\" title=\"엮인글\"></iframe>\n" + 
				"\n" + 
				"                        \n" + 
				"                        \n" + 
				"                            \n" + 
				"							\n" + 
				"								<script src=\"https://ssl.pstatic.net/tveta/libs/glad/prod/gfp-core.js\"></script>\n" + 
				"							\n" + 
				"							\n" + 
				"                            <script>var gAdPostUnitIdForPC = \"pc_blog_bottom\";</script>\n" + 
				"                            <script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//js/post/ssp/SspAdPostCallerForPC-c5c8185_https.js\"></script>\n" + 
				"							<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//js/post/ssp/SspAdPostCallerForPCBackup-79e9bc3_https.js\"></script>\n" + 
				"\n" + 
				"                            \n" + 
				"                            \n" + 
				"                                <script>var gAdContentUnitIdForPC = \"pc_blog_body\";</script>\n" + 
				"								<script>var gAdContentDaUnitIdForPc = \"pc_blog_body_2nd\";</script>\n" + 
				"								<script>var gAdContentDaUnitIdForPc2 = \"pc_blog_body_3rd\";</script>\n" + 
				"								<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//js/post/ssp/SspAdContentCallerForPC-3263896_https.js\"></script>\n" + 
				"								<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//js/post/ssp/SspAdContentCallerForPCBackup-20b0e90_https.js\"></script>\n" + 
				"                            \n" + 
				"\n" + 
				"                            <div id=\"ssp-adpost\"></div>\n" + 
				"                            <div  class=\"_adpost_skin_property\" style=\"position:absolute;z-index:-100;visibility:hidden\">\n" + 
				"                                <span class=\"pcol1\"></span>\n" + 
				"                                <span class=\"pcol2\"></span>\n" + 
				"                                <span class=\"pcol3\"></span>\n" + 
				"                            </div>\n" + 
				"                        \n" + 
				"					</td>\n" + 
				"                    <td class=\"bcr\" nowrap=\"nowrap\"></td>\n" + 
				"				</tr>\n" + 
				"			</table>\n" + 
				"			<table class=\"post-footer\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"ftl\" nowrap=\"nowrap\"></td><td class=\"ftc\"></td><td class=\"ftr\" nowrap=\"nowrap\"></td></tr></table>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"	<div class=\"division-line-x plile\"></div>\n" + 
				"	\n" + 
				"		\n" + 
				"		\n" + 
				"		\n" + 
				"	\n" + 
				"	\n" + 
				"    \n" + 
				"		\n" + 
				"		\n" + 
				"			\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//lib/egjs/visible.pkgd.min-7735f57_https.js\"></script>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"function ImageLazyLoader(selectors) {\n" + 
				"   this.selectors = selectors;\n" + 
				"}\n" + 
				"\n" + 
				"var imageLazyLoadVisible = new eg.Visible(document, {\n" + 
				"    targetClass : \"egjs-visible\",\n" + 
				"    expandSize : \"100\"\n" + 
				"});\n" + 
				"\n" + 
				"var imageLoad = function(el) {\n" + 
				"    var img = $Element(el);\n" + 
				"\n" + 
				"    // gifmp4인 경우 재생처리\n" + 
				"    if (img.hasClass(\"_gifmp4\") && img._element.tagName === \"VIDEO\" && img._element.paused) {\n" + 
				"        img._element.play().catch(function(){\n" + 
				"            var gifUrl = img.attr(\"data-gif-url\");\n" + 
				"            if (!gifUrl) {\n" + 
				"                return;\n" + 
				"            }\n" + 
				"            img.attr(\"poster\", gifUrl);\n" + 
				"        });\n" + 
				"        return;\n" + 
				"    }\n" + 
				"\n" + 
				"    var realSrc = img.attr(\"data-lazy-src\");\n" + 
				"    if (!realSrc) {\n" + 
				"        return;\n" + 
				"    }\n" + 
				"\n" + 
				"    img.attr(\"src\", realSrc);\n" + 
				"    img.attr(\"data-lazy-src\", \"\");\n" + 
				"};\n" + 
				"\n" + 
				"ImageLazyLoader.prototype.loadImages = function () {\n" + 
				"    var containers = this.selectors instanceof HTMLElement ? [this.selectors] : jindo.$$(this.selectors);\n" + 
				"    $A(containers).forEach($Fn(function (element) {\n" + 
				"        this.loadImagesForEachContainer($Element(element));\n" + 
				"    }, this).bind());\n" + 
				"\n" + 
				"    imageLazyLoadVisible.on(\"change\", function(e) {\n" + 
				"        e.visible.forEach(imageLoad);\n" + 
				"    });\n" + 
				"\n" + 
				"    imageLazyLoadVisible.check();\n" + 
				"};\n" + 
				"\n" + 
				"\n" + 
				"ImageLazyLoader.prototype.loadImagesForEachContainer = function (postContentArea) {\n" + 
				"    $A(postContentArea.queryAll(\"img\")).forEach($Fn(function (imgEl) {\n" + 
				"        $Element(imgEl).addClass(\"egjs-visible\");\n" + 
				"    }, this).bind());\n" + 
				"\n" + 
				"    $A(postContentArea.queryAll(\"video._gifmp4\")).forEach($Fn(function (imgEl) {\n" + 
				"        $Element(imgEl).addClass(\"egjs-visible\");\n" + 
				"    }, this).bind());\n" + 
				"\n" + 
				"    //marquee tag안에 이미지가 있을경우 이미지가 loading되지 않는 현상이 발생하여 예외처리 진행\n" + 
				"    $A(postContentArea.queryAll(\"marquee\")).forEach(function (marqueeEl) {\n" + 
				"        $A($Element(marqueeEl).queryAll(\"img\")).forEach(function (img) {\n" + 
				"            imageLoad(img);\n" + 
				"        });\n" + 
				"    });\n" + 
				"};\n" + 
				"\n" + 
				"var imageLazyLoader = new ImageLazyLoader(\"#postListBody .post, .post.prologue\");\n" + 
				"imageLazyLoader.loadImages();\n" + 
				"\n" + 
				"$Fn(function() {\n" + 
				"    $Fn(function(){\n" + 
				"        imageLazyLoadVisible.check();\n" + 
				"    }).attach(window, \"scroll\");\n" + 
				"}).attach(window, \"domready\");\n" + 
				"\n" + 
				"$Fn(function() {\n" + 
				"    imageLazyLoadVisible.check();\n" + 
				"}).attach(window, \"load\");\n" + 
				"\n" + 
				"</script>\n" + 
				"		\n" + 
				"    \n" + 
				"</div>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"	\n" + 
				"\n" + 
				"	var isConvertEnabled = 'true' === 'true';\n" + 
				"	var isDisplayAd2 = true;\n" + 
				"\n" + 
				"	function closeCompletePopup() {\n" + 
				"		var lyr_popup = document.getElementById(\"challengePopLayer\");\n" + 
				"		lyr_popup.style.display = 'none';\n" + 
				"		window.document.body.style.overflow = '';\n" + 
				"	}\n" + 
				"\n" + 
				"	function suggestConvert(logNo, isSupportSE3, scrapPostYn){\n" + 
				"\n" + 
				"		if(isConvertEnabled && !isSupportSE3) {\n" + 
				"			if(confirm(\"스마트에디터 ONE으로 글을 수정합니다.\\n발행 전에 글감이 잘 노출되는지 확인해 주세요.\")){\n" + 
				"				location.href=\"wood-24/postupdate?logNo=\" + logNo + \"&isConvert=true\";\n" + 
				"			}\n" + 
				"		} else {\n" + 
				"			location.href=\"wood-24/postupdate?logNo=\" + logNo;\n" + 
				"		}\n" + 
				"	}\n" + 
				"</script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"    \n" + 
				"    \n" + 
				"    \n" + 
				"    \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"postListBottom\" style=\"display: block;\" class=\"other_category_and_popular_expose other_category_and_popular_click\">\n" + 
				"	<div class=\"post\">\n" + 
				"		<div class=\"post-back\">\n" + 
				"			<table cellspacing=\"0\" cellpadding=\"0\" class=\"post-head\" role=\"presentation\">\n" + 
				"				<tbody>\n" + 
				"					<tr>\n" + 
				"						<td class=\"htl\"></td>\n" + 
				"						<td class=\"htc\"></td>\n" + 
				"						<td class=\"htr\"></td>\n" + 
				"					</tr>\n" + 
				"				</tbody>\n" + 
				"			</table>\n" + 
				"			<table cellspacing=\"0\" cellpadding=\"0\" class=\"post-body\" role=\"presentation\">\n" + 
				"				<tbody>\n" + 
				"					<tr>\n" + 
				"						<td class=\"bcl\"></td>\n" + 
				"						<td class=\"bcc\">\n" + 
				"						\n" + 
				"							<div class=\"wrap_blog2_list wrap_blog2_thumblist other_category_album_list_expose other_category_album_list_click\">\n" + 
				"								<div class=\"wrap_title\">\n" + 
				"									<h4 class=\"title pcol2\">\n" + 
				"										이 블로그\n" + 
				"										\n" + 
				"											\n" + 
				"											\n" + 
				"												\n" + 
				"													\n" + 
				"													\n" + 
				"													<a href=\"/PostList.naver?blogId=wood-24&categoryNo=12&from=thumbnailList&parentCategoryNo=0\" class=\"pcol2 aggregate_click_delegate\"> <strong class=\"pcol3\">시공 포트폴리오</strong> </a>\n" + 
				"													\n" + 
				"												\n" + 
				"											\n" + 
				"										\n" + 
				"										카테고리 글\n" + 
				"									</h4>\n" + 
				"									\n" + 
				"										\n" + 
				"										\n" + 
				"											\n" + 
				"												\n" + 
				"												\n" + 
				"													<a href=\"/PostList.naver?blogId=wood-24&categoryNo=12&from=thumbnailList&parentCategoryNo=0\" class=\"btn_openlist pcol2 aggregate_click_delegate\" onclick=\"nclk_v2(this,'rec_others.all','','');\">전체글 보기</a>\n" + 
				"												\n" + 
				"											\n" + 
				"										\n" + 
				"									\n" + 
				"								</div>\n" + 
				"								<div class=\"wrap_list\">\n" + 
				"									<div class=\"blog2_thumblist\">\n" + 
				"										<ul class=\"thumblist\" id=\"postBottomThumbnailTitleListBody\">\n" + 
				"										</ul>\n" + 
				"									</div>\n" + 
				"									<div class=\"wrap_blog2_paginate\">\n" + 
				"										<div class=\"blog2_paginate\" id=\"postBottomThumbnailTitleListNavigation\">\n" + 
				"										</div>\n" + 
				"									</div>\n" + 
				"								</div>\n" + 
				"                            \n" + 
				"                                <div class=\"area_btn_top\">\n" + 
				"                                    <a href=\"#\" class=\"btn_top pcol2\" role=\"button\" onclick=\"nclk_v2(this, 'rec.top', '', '', event)\"><i class=\"icon\"></i><span class=\"blind\">화면 최상단으로 이동</span></a>\n" + 
				"                                </div>\n" + 
				"                            \n" + 
				"							</div>\n" + 
				"						\n" + 
				"						\n" + 
				"						\n" + 
				"						</div>\n" + 
				"\n" + 
				"						</td>\n" + 
				"						<td class=\"bcr\"></td>\n" + 
				"					</tr>\n" + 
				"				</tbody>\n" + 
				"			</table>\n" + 
				"			<table cellspacing=\"0\" cellpadding=\"0\" class=\"post-footer\" role=\"presentation\">\n" + 
				"				<tbody>\n" + 
				"					<tr>\n" + 
				"						<td class=\"ftl\"></td>\n" + 
				"						<td class=\"ftc\"></td>\n" + 
				"						<td class=\"ftr\"></td>\n" + 
				"					</tr>\n" + 
				"				</tbody>\n" + 
				"			</table>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/PostViewBottom-747715564_https.js\" charset=\"UTF-8\"></script>\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"	\n" + 
				"	postViewBottomTitleListController.start(blogId, nFirstLogNo, 'false' == 'true', parentCategoryNo, currentCategoryNo, categoryName, viewDate, '1700092800000', from, 'true', '4');\n" + 
				"</script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"    \n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"    var fromClosedPost = \"\" === \"true\";\n" + 
				"    var isStage = \"false\" === \"true\";\n" + 
				"    var blogRealHttpDomain = \"http://blog.naver.com\";\n" + 
				"	var nilOptions = {\n" + 
				"        devMode: \"true\"==\"false\",\n" + 
				"            topReferer : (function() {\n" + 
				"                if (fromClosedPost) {\n" + 
				"                    return '';\n" + 
				"                }\n" + 
				"\n" + 
				"                var ref = \"https:\\/\\/section.blog.naver.com\\/BlogHome.naver?directoryNo=0&currentPage=1&groupId=0\";\n" + 
				"                var isMobile = function() {\n" + 
				"                    return window.location.host.indexOf('m.blog.naver.com') >= 0;\n" + 
				"                };\n" + 
				"\n" + 
				"                if (isMobile()){\n" + 
				"                    return document.referrer;\n" + 
				"                } else {\n" + 
				"                    if (ref == '') {\n" + 
				"                        if (parent != window && parent.location.href == document.referrer) {\n" + 
				"                            return '';\n" + 
				"                        } else {\n" + 
				"                            return document.referrer;\n" + 
				"                        }\n" + 
				"                    } else {\n" + 
				"                        return ref;\n" + 
				"                    }\n" + 
				"                }\n" + 
				"            })(),\n" + 
				"        proxyUse:\"true\" === \"true\"\n" + 
				"	}\n" + 
				"</script>\n" + 
				"<div id=\"multime2Layer\" class=\"layer_style display_none\"></div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"	\n" + 
				"	\n" + 
				"	\n" + 
				"\n" + 
				"	<script id=\"socialPluginCustomTag\" src=\"https://ssl.pstatic.net/spi/js/release/ko_KR/splugin.js?v=1416757.0\" charset=\"utf-8\"></script>\n" + 
				"	<script>\n" + 
				"	window.onload = function() {\n" + 
				"        window.__splugin = SocialPlugIn_Core({\n" + 
				"            //소셜 플러그인 기본 설정\n" + 
				"            // nEvent Key\n" + 
				"            \"evKey\"       : \"blog\",\n" + 
				"            // 서비스명\n" + 
				"            \"serviceName\" : \"블로그\",\n" + 
				"            // 출처\n" + 
				"            \"sourceName\"  : \"\\uC6B0\\uB4DC24\",\n" + 
				"            \"sendNClick\"  : 'on',\n" + 
				"            \"nClickCode\" : {\n" + 
				"                'blog': { 'in': 'shr_lyr.blog', 'out': 'shr.blog' },\n" + 
				"                'cafe': { 'in': 'shr_lyr.cafe', 'out': 'shr.cafe' },\n" + 
				"                'bookmark': { 'in': 'shr_lyr.bookmark', 'out': 'shr.bookmark' },\n" + 
				"                'memo':     { 'in': 'shr_lyr.memo',     'out': 'shr.memo' },\n" + 
				"                'mail':     { 'in': 'shr_lyr.mail',     'out': 'shr.mail' },\n" + 
				"                'band':     { 'in': 'shr_lyr.band',     'out': 'shr.band' },\n" + 
				"                'line':     { 'in': 'shr_lyr.line',     'out': 'shr.line' },\n" + 
				"                'twitter':  { 'in': 'shr_lyr.twitter',  'out': 'shr.twitter' },\n" + 
				"                'facebook': { 'in': 'shr_lyr.facebook', 'out': 'shr.facebook' },\n" + 
				"                'weibo':    { 'in': 'shr_lyr.weibo',    'out': 'shr.weibo' },\n" + 
				"                'reddit':   { 'in': 'shr_lyr.reddit',   'out': 'shr.reddit' },\n" + 
				"                'copyurl':  { 'in': 'shr_lyr.copyurl' },\n" + 
				"                'release':  { 'out': 'shr.sharebtn' }\n" + 
				"        	}\n" + 
				"        });\n" + 
				"	}\n" + 
				"	</script>\n" + 
				"\n" + 
				"\n" + 
				"    <script type=\"text/javascript\" src=\"https://editor-static.pstatic.net/v/basic/1.50.0/se.viewer.js?v=1.50.0-20231027170641\" defer></script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"    var isCommentUseProxy = \"true\" === \"true\"\n" + 
				"</script>\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/PostBottomCommon-750867535_https.js\" charset=\"utf-8\"></script>\n" + 
				"\n" + 
				"    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://apollo-cdn.pstatic.net/influencer-sdk/influencer-sdk-0.2.0.css\">\n" + 
				"    <script type=\"text/javascript\" src=\"https://apollo-cdn.pstatic.net/influencer-sdk/influencer-sdk-0.2.0.cdn.js\"></script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//lib/prismplayer/prismplayer-pc-se-0.4.5-7dfe3be_https.css\">\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"new InstallBlogAppPush();\n" + 
				"</script>\n" + 
				"\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"    <script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/versioning_nil_ntm-9349908_https.js\" charset=\"UTF-8\"></script>\n" + 
				"    \n" + 
				"    <script type=\"text/javascript\">\n" + 
				"        var urlDomain = 'http://blog.naver.com';\n" + 
				"\n" + 
				"        // stage의 경우 real과 동일하게 집계되게 하기 위하여 real로 처리\n" + 
				"        if(isStage){\n" + 
				"            urlDomain = blogRealHttpDomain;\n" + 
				"        }\n" + 
				"\n" + 
				"        var nilNtmLibUrl = \"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/nilForNtm-89afd2f_https.js\";\n" + 
				"        var gdid = '90000003_0000000000000033FBA0409A';\n" + 
				"        var sBlogIdForNil = 'wood-24';\n" + 
				"        var uri = urlDomain + '/wood-24/223264915610';\n" + 
				"        var relationType = 4;\n" + 
				"        var blogOwnerRelationType = 4;\n" + 
				"        var referrer = nilOptions.topReferer;\n" + 
				"        var dimension_1 = \"%EC%9D%B8%ED%85%8C%EB%A6%AC%EC%96%B4%C2%B7DIY\";\n" + 
				"        var optional_1 = false;\n" + 
				"        var event_category = \"post_view\";\n" + 
				"        var isNilAnalystLeaveEvent = \"true\";\n" + 
				"        var isPostList = !!isPostList;\n" + 
				"        // postList 인 경우, 첫번째 포스트에 대한 상품 첨부 여부를 보낸다.\n" + 
				"        var firstPostHasProduct = false;\n" + 
				"\n" + 
				"        // blog-proxy 서버 사용\n" + 
				"        if(nilOptions.proxyUse) {\n" + 
				"            sBlogIdForNil = \"wood-24\";\n" + 
				"            uri = urlDomain + '/wood-24/223264915610';\n" + 
				"        }\n" + 
				"        $Fn(function () {\n" + 
				"            try {\n" + 
				"                new nilNtmForBlogCVAnalyst(sBlogIdForNil, uri, relationType, gdid, dimension_1, referrer, optional_1, event_category, nilNtmLibUrl, isPostList, isNilAnalystLeaveEvent, firstPostHasProduct);\n" + 
				"            } catch (e) {}\n" + 
				"        }).attach(window, \"load\");\n" + 
				"\n" + 
				"    </script>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\" charset=\"utf-8\" src=\"https://static-like.pstatic.net/js/reaction/dist/reaction.min.js?v=1416757.0\"></script>\n" + 
				"<script src=\"https://ssl.pstatic.net/tveta/libs/ssp-video/prod/ssp.web.sdk.js\"></script>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"\n" + 
				"\n" + 
				"$Fn(function() {\n" + 
				"	try {\n" + 
				"        reaction.init({\n" + 
				"            type: \"basic\",\n" + 
				"            domain : \"https://blog.like.naver.com\",\n" + 
				"            staticDomain : \"https://static-like.pstatic.net\",\n" + 
				"            cssId : \"blog\",\n" + 
				"            isMobile : false,\n" + 
				"            maxCount : 9999,\n" + 
				"            isHiddenCount : false,\n" + 
				"            highlightCount : 50,\n" + 
				"            highlightClassname : \"double_heart\",\n" + 
				"            \n" + 
				"            isUseApigw : true,\n" + 
				"            apigwInfo : {\n" + 
				"                domain : \"https://apis.naver.com/blogserver/like\",\n" + 
				"                pool : \"blogid\"\n" + 
				"            },\n" + 
				"            \n" + 
				"            callback : {\n" + 
				"                click : function(param){\n" + 
				"                    if (gbIsNotOpenBlogUser) {\n" + 
				"                        var oOptions = {\n" + 
				"                            wrapperDivId: \"domainRegisterWrap\"\n" + 
				"                            , title: \"공감을 남기려면 블로그 아이디가 필요해요.\"\n" + 
				"                            , confirmBtnValue: \"아이디 만들기\"\n" + 
				"                            , win: window\n" + 
				"                            , isLightVersion: true\n" + 
				"                            , clickCodePrefix : \"bsu*l.like\"\n" + 
				"                            , initialCode : \"bsu*l.like\"\n" + 
				"                            , callbackFn: function () {\n" + 
				"                                location.reload();\n" + 
				"                            },\n" + 
				"                            apiUrl: {\n" + 
				"                                recommendDomainListUrl: \"/blogdomain/RecommendBlogDomainList.naver\",\n" + 
				"                                duplicateCheckUrl: \"/blogdomain/BlogDomainDuplicateCheck.naver\",\n" + 
				"                                blogDomainRegisterUrl: \"/blogdomain/BlogDomainRegistration.naver\"\n" + 
				"                            }\n" + 
				"                        };\n" + 
				"                        blogDomainRegisterLayer.init(oOptions);\n" + 
				"                        blogDomainRegisterLayer.show();\n" + 
				"                        return false;\n" + 
				"                    }\n" + 
				"                    oSympathyLayer.setLogno(param);\n" + 
				"                },\n" + 
				"                clicked : function(){\n" + 
				"                    oSympathyLayer.reloadSympathyLayer();\n" + 
				"                }\n" + 
				"            }\n" + 
				"        });\n" + 
				"	} catch(e){}\n" + 
				"\n" + 
				"    $ElementList(\".u_likeit_list_btn\").addClass(\"pcol2\");\n" + 
				"    $ElementList(\"#floating_bottom .u_likeit_list_btn\").removeClass(\"pcol2\");\n" + 
				"\n" + 
				"}).attach(window, \"load\");\n" + 
				"\n" + 
				"function showBlogDomainRegisterLayer(event){\n" + 
				"    event.preventDefault();\n" + 
				"    event.stopPropagation();\n" + 
				"    var oOptions = {\n" + 
				"        wrapperDivId: \"domainRegisterWrap\"\n" + 
				"        /*, registerBtnEl: angular.element(document.querySelector(attr['bgBlogDomainRegisterBtnId']))*/\n" + 
				"        , title: \"사용하실 블로그 아이디를 입력해주세요.\"\n" + 
				"        , confirmBtnValue: \"확인\"\n" + 
				"        , win: window\n" + 
				"        , isChangeLayer: false\n" + 
				"        , clickCodePrefix : \"bsu*i.\"\n" + 
				"        , initialCode: \"bsu*i\"\n" + 
				"        , callbackFn: function () {\n" + 
				"            location.reload();\n" + 
				"        },\n" + 
				"        closeFn: function () {\n" + 
				"            location.reload();\n" + 
				"        },\n" + 
				"        apiUrl: {\n" + 
				"            recommendDomainListUrl: \"/blogdomain/RecommendBlogDomainList.naver\",\n" + 
				"            duplicateCheckUrl: \"/blogdomain/BlogDomainDuplicateCheck.naver\",\n" + 
				"            blogDomainRegisterUrl: \"/blogdomain/BlogDomainRegistration.naver\",\n" + 
				"            blogBasicInfoUpdateUrl: \"/blogdomain/BlogBasicInfoUpdate.naver\",\n" + 
				"            profileImageUploadUrl: \"/blogdomain/ProfileImageUpload.naver\",\n" + 
				"            profileImageUploadFromUrlUrl: \"/blogdomain/ProfileImageUploadFromUrl.naver\",\n" + 
				"            blogInfoUrl: \"/blogdomain/BlogBasicInfo.naver\",\n" + 
				"            recommendBlogByPersonalThemeListUrl: \"/blogdomain/RecommendBlogByPersonalThemeList.naver\",\n" + 
				"            recommendBlogAddBuddyUrl: \"/blogdomain/SimpleBuddyAdd.naver\",\n" + 
				"            recommendBlogRemoveBuddyUrl: \"/blogdomain/SimpleBuddyRemove.naver\",\n" + 
				"            profileDefaultImageUrl: \"/blogdomain/ProfileDefaultImage.naver\",\n" + 
				"            personalThemeUpdateUrl: \"/blogdomain/SimplePersonalThemeUpdate.naver\",\n" + 
				"            personalThemeListUrl:\"/blogdomain/SimplePersonalThemeList.naver\"\n" + 
				"        },\n" + 
				"        blogUrl: blogURL\n" + 
				"    };\n" + 
				"    blogDomainRegisterLayer.init(oOptions);\n" + 
				"    blogDomainRegisterLayer.showSuggestionLayer();\n" + 
				"}\n" + 
				"\n" + 
				"function showBlogDomainRegisterLayerBuddyAdd() {\n" + 
				"    var oOptions = {\n" + 
				"        wrapperDivId: \"domainRegisterWrap\"\n" + 
				"        , title: \"이웃을 추가하려면 블로그 아이디가 필요해요.\"\n" + 
				"        , confirmBtnValue: \"아이디 만들기\"\n" + 
				"        , win: window\n" + 
				"        , isLightVersion: true\n" + 
				"        , clickCodePrefix : \"bsu*l.add\"\n" + 
				"        , initialCode: \"bsu*l.add\"\n" + 
				"        , callbackFn: function () {\n" + 
				"            location.reload();\n" + 
				"        },\n" + 
				"        apiUrl: {\n" + 
				"            recommendDomainListUrl: \"/blogdomain/RecommendBlogDomainList.naver\",\n" + 
				"            duplicateCheckUrl: \"/blogdomain/BlogDomainDuplicateCheck.naver\",\n" + 
				"            blogDomainRegisterUrl: \"/blogdomain/BlogDomainRegistration.naver\"\n" + 
				"        }\n" + 
				"    };\n" + 
				"    blogDomainRegisterLayer.init(oOptions);\n" + 
				"    blogDomainRegisterLayer.show();\n" + 
				"}\n" + 
				"baPostOnlyViewEvent(131720049, 223264915610);\n" + 
				"</script>\n" + 
				"\n" + 
				"          				\n" + 
				"          				\n" + 
				"          			\n" + 
				"\n" + 
				"				\n" + 
				"		  	\n" + 
				"</div>\n" + 
				"\n" + 
				"							</div><hr />\n" + 
				"							\n" + 
				"							\n" + 
				"							<div id=\"left-area\" class=\"side-width side-width-1\"><div class=\"side-border\"><div class=\"side-head\"></div><div class=\"side-body\">\n" + 
				"								\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"blog-profile\" class=\"widget\">\n" + 
				"	\n" + 
				"	<p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"blog-category\" class=\"widget\">\n" + 
				"\n" + 
				"<input type=\"hidden\" id=\"categoryNo\" name=\"categoryNo\" value=\"\" ></input>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	\n" + 
				"		\n" + 
				"	\n" + 
				"	\n" + 
				"	\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"blog-search\" class=\"widget\">\n" + 
				"	<div class=\"blog_search\">\n" + 
				"		<form name=\"searchfrm\" id=\"searchfrm\"  method=\"GET\" action=\"https://blog.naver.com/PostSearchList.naver\">\n" + 
				"		<h3 class=\"blind\">검색</h3>\n" + 
				"		<label for=\"searchText\" class=\"blind\">글 검색</label><input type=\"text\" id=\"searchText\" name=\"SearchText\" class=\"inp\" style=\"ime-mode:active;\" value=\"\" onclick=\"nclk_v2(this,'sew.window','','');\"/><input type=\"hidden\" name=\"blogId\" value=\"wood-24\" /><input type=\"image\" src=\"https://blogimgs.pstatic.net/nblog/spc.gif\" width=\"1\" height=\"1\" class=\"sp_blog btn\" id=\"searchBtn\" alt=\"검색\" onclick=\"nclk_v2(this,'sew.search','','');\"/>\n" + 
				"		</form>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"\n" + 
				"<div class=\"widget\" style=\"height:600px\" id=\"externalwidget_7\">\n" + 
				"	<iframe id=\"ExternalWidgetIframe_7\" name=\"ExternalWidgetIframe_7\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" style=\"width:170px;display:none;\" title=\"외부위젯\"></iframe>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"<div class=\"widget\" style=\"height:244px\" id=\"externalwidget_10\">\n" + 
				"	<iframe id=\"ExternalWidgetIframe_10\" name=\"ExternalWidgetIframe_10\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" style=\"width:170px;display:none;\" title=\"외부위젯\"></iframe>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"<div class=\"widget\" style=\"height:99px\" id=\"externalwidget_8\">\n" + 
				"	<iframe id=\"ExternalWidgetIframe_8\" name=\"ExternalWidgetIframe_8\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" style=\"width:170px;display:none;\" title=\"외부위젯\"></iframe>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"<div class=\"widget\" style=\"height:197px\" id=\"externalwidget_9\">\n" + 
				"	<iframe id=\"ExternalWidgetIframe_9\" name=\"ExternalWidgetIframe_9\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" style=\"width:170px;display:none;\" title=\"외부위젯\"></iframe>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"<div class=\"widget cmp_wrap\" id=\"widget_business\" style=\"overflow:visible\"></div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"blog-rss\" class=\"widget\">\n" + 
				"  <a href=\"https://rss.blog.naver.com/wood-24.xml\" target=\"_blank\" class=\"rss1\" onclick=\"nclk_v2(this,'rsw.rss20','','');\"><span class=\"blind\">RSS 2.0</span></a>\n" + 
				"  <a href=\"https://rss.blog.naver.com/wood-24.xml?rss=1.0\" target=\"_blank\" class=\"rss2\" onclick=\"nclk_v2(this,'rsw.rss10','','');\"><span class=\"blind\">RSS 1.0</span></a>\n" + 
				"  <a href=\"https://rss.blog.naver.com/wood-24.xml?atom=0.3\" target=\"_blank\" class=\"rss3\" onclick=\"nclk_v2(this,'rsw.atom03','','');\"><span class=\"blind\">ATOM 0.3</span></a>\n" + 
				"</div>\n" + 
				"<div class=\"division-line-x\"></div>\n" + 
				"\n" + 
				"							</div><div class=\"side-footer\"></div></div></div>\n" + 
				"							\n" + 
				"						</div>\n" + 
				"						<div id=\"bottom-area\">\n" + 
				"				      		\n" + 
				"						</div>\n" + 
				"					</div><hr />\n" + 
				"				</div>\n" + 
				"				<div id=\"whole-footer\"></div>\n" + 
				"			</div>\n" + 
				"			<div id=\"blog-sign\"></div>\n" + 
				"		</div>\n" + 
				"		\n" + 
				"		<div id=\"bottom-skin\"></div>\n" + 
				"\n" + 
				"		\n" + 
				"		\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"	</div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"avoid_facebook_crawling_layer\" style=\"display:none;\">\n" + 
				"	<p>안녕하세요.<br/>이 포스트는 네이버 블로그에서 작성된 게시글입니다.<br/>자세한 내용을 보려면 링크를 클릭해주세요.<br/>감사합니다.</p>\n" + 
				"</div>\n" + 
				"\n" + 
				"<div id=\"travel_closing_popup\" class=\"travel_closing_popup\" style=\"width:350px; display:none; cursor: default; position:absolute;\">\n" + 
				"	<div class=\"border_type\">\n" + 
				"		<div class=\"content3\">\n" + 
				"			<h5 class=\"gr\" style=\"margin:10;\">글 보내기 서비스 안내</h5>\n" + 
				"			<p class=\"ulayer\" style=\"margin:10;\">2009년 6월 30일 네이버 여행 서비스가 종료되었습니다.<br/> 네이버 여행 서비스를 이용해 주신 여러분께 감사드리며, <br/>더 좋은 서비스로 보답할 수 있도록 노력하겠습니다.<br/></p>\n" + 
				"			<div align=\"center\"><a href=\"#\" class=\"btn_confirm _returnFalse _closeLayer _param(travel_closing_popup)\"><img src=\"https://ssl.pstatic.net/static/common/btn/btn_confirm2.gif\" border=\"0\" alt=\"확인\"></a></div>\n" + 
				"			<br />\n" + 
				"			<a href=\"#\" class=\"closelayer _returnFalse _closeLayer _param(travel_closing_popup)\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_thin_close.gif\" width=\"13\" height=\"10\" alt=\"닫기\" class=\"btn_close\"></a>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div style=\"display:none;\" class=\"addfile_layer\" id=\"fileLayer\">\n" + 
				"	<div class=\"layer_contents\"><ul><li></li></ul>\n" + 
				"	<a href=\"#\" class=\"clse _closeLayer _param(fileLayer) _returnFalse\"><img width=\"9\" height=\"9\" class=\"btn_close\" src=\"https://blogimgs.pstatic.net/imgs/btn_close8.gif\" alt=\"닫기\"/></a>\n" + 
				"	</div>\n" + 
				"	<span class=\"shadow shadow1\"></span><span class=\"shadow shadow2\"></span><span class=\"shadow shadow3\"></span>\n" + 
				"</div>\n" + 
				"\n" + 
				"	\n" + 
				"	<div id=\"layer_msg\">\n" + 
				"	<div class=\"layer_hashfilter\" id=\"malwareAlertLayer\" style=\"width:390px;display:none;z-index:99999\">\n" + 
				"		<h2>악성코드가 포함되어 있는 파일입니다.</h2>\n" + 
				"		<p class=\"file\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/ico_file.gif\" width=\"11\" height=\"10\" alt=\"파일\">{FILENAME}</p>\n" + 
				"\n" + 
				"		<p>백신 프로그램으로 치료하신 후 다시 첨부하시거나, 치료가 어려우시면<br>파일을 삭제하시기 바랍니다.</p> <!-- 100513 -->\n" + 
				"		<a href=\"http://security.naver.com/index.nhn\" target=\"_blank\" class=\"vaccine_link\">네이버 백신으로 치료하기</a>\n" + 
				"		<p class=\"info_text\">고객님의 PC가 악성코드에 감염될 경우 시스템성능 저하,<br>개인정보 유출등의 피해를 입을 수 있으니 주의하시기 바랍니다.</p>\n" + 
				"		<div class=\"btns\">\n" + 
				"			<a href=\"FILEPATH\" class=\"downloadBtn\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_download2.gif\" width=\"73\" height=\"26\" alt=\"다운로드\"></a>\n" + 
				"			<a href=\"#\" class=\"_closeMalwareAlertLayer _returnFalse\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_cancel3.gif\" width=\"45\" height=\"26\" alt=\"취소\"></a>\n" + 
				"		</div>\n" + 
				"		<a href=\"#\" class=\"close _closeMalwareAlertLayer _returnFalse\"><img src=\"https://blogimgs.pstatic.net/imgs/btn_close3.gif\" width=\"19\" height=\"19\" border=\"0\" alt=\"닫기\"></a>\n" + 
				"	</div>\n" + 
				"	\n" + 
				"	<div class=\"layer_hashfilter layer_filter2\" id=\"blockAlertLayer\" style=\"display:none;z-index:99999\">\n" + 
				"		<h2>작성자 이외의 방문자에게는 이용이 제한되었습니다.</h2>\n" + 
				"		<p>{ALERTMESSAGE}</p>\n" + 
				"		<p class=\"rst\">이용제한 파일 : {FILENAME}</p>\n" + 
				"\n" + 
				"		<div class=\"btns\">\n" + 
				"			<a href=\"FILEPATH\" class=\"downloadBtn\" id=\"blockAlertDownloadBtn\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_download2.gif\" width=\"73\" height=\"26\" alt=\"다운로드\"></a>\n" + 
				"			<a href=\"#\" class=\"_closeBlockAlertLayer _returnFalse\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_cancel3.gif\" width=\"45\" height=\"26\" alt=\"취소\"></a>\n" + 
				"		</div>\n" + 
				"		<a href=\"#\" class=\"close _closeBlockAlertLayer _returnFalse\"><img src=\"https://blogimgs.pstatic.net/imgs/btn_close3.gif\" width=\"19\" height=\"19\" border=\"0\" alt=\"닫기\"></a>\n" + 
				"	</div>\n" + 
				"	</div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"sendInfoLayer\" style=\"display:none;\" class=\"send_post_layer\">\n" + 
				"	<div class=\"layer_contents\" id=\"sendInfoLayerDiv\"></div>\n" + 
				"	<a href=\"#\"><img width=\"13\" height=\"12\" class=\"btn_close\" src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_close5.gif\" alt=\"닫기\"/></a>\n" + 
				"	<span class=\"shadow shadow\"></span><span class=\"shadow shadow2\"></span><span class=\"shadow shadow3\"></span>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div style=\"position:absolute; top:0; left: -10000px; width: 320px;\" class=\"post_layer\" id=\"sendPostLayer\">\n" + 
				"	<div class=\"shadow01\">\n" + 
				"	<div class=\"shadow01_side\">\n" + 
				"	<div class=\"shadow02\">\n" + 
				"	<div class=\"shadow02_side\">\n" + 
				"		<div class=\"border_type\">\n" + 
				"			<div class=\"content\" id=\"layerContent\"></div>\n" + 
				"			<div class=\"btn\">\n" + 
				"				<a href=\"#\" id=\"sendPostLayerBtn\" class=\"_deletePostConfirm _returnFalse _closeLayer _param(sendPostLayer)\" target=\"_blank\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_confirm.gif\" width=\"38\" height=\"21\" alt=\"확인\"></a>\n" + 
				"				<a href=\"#\" class=\"_closeLayer _param(sendPostLayer) _returnFalse\"><img src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_cancel.gif\" width=\"38\" height=\"21\" alt=\"취소\"></a>\n" + 
				"			</div>\n" + 
				"			<a href=\"#\" class=\"close _closeLayer _param(sendPostLayer) _returnFalse\"><img src=\"https://blogimgs.pstatic.net/nblog/btn_close_1.gif\" alt=\"닫기\" width=\"15\" height=\"14\"></a>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"	</div>\n" + 
				"	</div>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<input type=\"hidden\" name=\"logtype\" id=\"logtype\" value=\"mylog\"></input>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<form id=\"saveTagName\" name=\"saveTagName\" method=\"post\" action=\"/BlogTagNameRegist.naver\" target=\"saveTagNameFrame\">\n" + 
				"	<input type=\"hidden\" name=\"blogId\" value=\"wood-24\" />\n" + 
				"	<input type=\"hidden\" name=\"logNo\" />\n" + 
				"	<input type=\"hidden\" name=\"chtagname\" />\n" + 
				"	<input type=\"hidden\" name=\"logType\" value=\"mylog\"/>\n" + 
				"	<input type=\"hidden\" name=\"directEdit\" value=\"Y\" />\n" + 
				"</form>\n" + 
				"\n" + 
				"\n" + 
				"<iframe name=\"saveTagNameFrame\" id=\"saveTagNameFrame\" src=\"\" style=\"display:none\" width=\"0\" height=\"0\" title=\"포스트별 태그정보 저장용 프레임\"></iframe>\n" + 
				"\n" + 
				"\n" + 
				"<form name=\"deletePostFrm\" method=\"post\" style=\"display:none;\" action=\"\">\n" + 
				"    <input type='hidden' name='blogId' value=''>\n" + 
				"    <input type='hidden' name='cpage' value=''>\n" + 
				"    <input type='hidden' name='categoryNo' value=''>\n" + 
				"    <input type='hidden' name='parentCategoryNo' value=''>\n" + 
				"    <input type='hidden' name='viewDate' value=''>\n" + 
				"    <input type='hidden' name='sourceCode' value=''>\n" + 
				"	<input type='hidden' name='logNo' value=''/>\n" + 
				"	<input type='hidden' name='currentPage' value=''/>\n" + 
				"</form>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"ExifInfoBtn\" style=\"display: none; position: absolute;\"\n" + 
				"	class=\"btn_exif\">\n" + 
				"	<a href=\"#\" class=\"_showExifInfoLayer _returnFalse\">\n" + 
				"		<img height=\"20\" width=\"80\" alt=\"사진정보 보기\" src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_viewexif.gif\" />\n" + 
				"	</a>\n" + 
				"</div>\n" + 
				"<div id=\"ExifInfoBtnWithDirectSave\" style=\"display: none; position: absolute;\" class=\"btn_exif\">\n" + 
				"	<a href=\"#\" class=\"_saveSourceImage _returnFalse\">\n" + 
				"		<img width=\"80\" height=\"20\" alt=\"원본 저장하기\" 	src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_originaldn.gif\">\n" + 
				"	</a>\n" + 
				"	<a href=\"#\" class=\"_showExifInfoLayer _returnFalse\">\n" + 
				"		<img height=\"20\" width=\"80\" alt=\"사진정보 보기\" 	src=\"https://blogimgs.pstatic.net/nblog/mylog/post/btn_viewexif.gif\" />\n" + 
				"	</a>\n" + 
				"	<ul id=\"SaveOption\" class=\"save_opt _saveOptionLayer\">\n" + 
				"		<li><a href=\"#\" class=\"toPC _returnFalse\">내PC 저장</a></li>\n" + 
				"		<li><a href=\"#\" class=\"toNDrive _returnFalse\">N드라이브 저장</a></li>\n" + 
				"	</ul>\n" + 
				"</div>\n" + 
				"\n" + 
				"<div id=\"ExifInfoLayer\" style=\"display: none;\" class=\"detail_exif\">\n" + 
				"	<a href=\"#\" class=\"_ellipsisHelper _ellipsis _param(120)\" style=\"display: none; font-size: 11px; font-family: '돋움', Dotum;\"></a>\n" + 
				"	<div>\n" + 
				"		<a href=\"#\" class=\"_closeLayer _param(ExifInfoLayer) _returnFalse\">\n" + 
				"			<img height=\"21\" width=\"21\" class=\"close _ExifCloseBtn\" alt=\"닫기\" src=\"https://blogimgs.pstatic.net/imgs/nblog/spc.gif\" />\n" + 
				"		</a> \n" + 
				"		<img height=\"14\" width=\"69\" class=\"tit\" alt=\"사진정보보기\" src=\"https://blogimgs.pstatic.net/nblog/mylog/post/tit_viewexif.gif\" /><br />\n" + 
				"		<dl>\n" + 
				"			<dt>카메라 모델</dt>\n" + 
				"			<dd class=\"_model\"></dd>\n" + 
				"			<dt>해상도</dt>\n" + 
				"			<dd class=\"_resolution\"></dd>\n" + 
				"			<dt>노출시간</dt>\n" + 
				"			<dd class=\"_exposure\"></dd>\n" + 
				"			<dt>노출보정</dt>\n" + 
				"			<dd class=\"_correct\"></dd>\n" + 
				"			<dt>프로그램모드</dt>\n" + 
				"			<dd class=\"_program\"></dd>\n" + 
				"			<dt>ISO감도</dt>\n" + 
				"			<dd class=\"_iso\"></dd>\n" + 
				"			<dt>조리개값</dt>\n" + 
				"			<dd class=\"_iris\"></dd>\n" + 
				"			<dt>초점길이</dt>\n" + 
				"			<dd class=\"_focus\"></dd>\n" + 
				"			<dt>측광모드</dt>\n" + 
				"			<dd class=\"_ttl\"></dd>\n" + 
				"			<dt>촬영일시</dt>\n" + 
				"			<dd class=\"_date\"></dd>\n" + 
				"		</dl>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<div class=\"cp_layer_popup\" style=\"width:370px; display:none\" id=\"sendPostBlockLayerById\">\n" + 
				"	<div class=\"shadow1\"><div class=\"shadow1_side\"><div class=\"shadow2\"><div class=\"shadow2_side\">\n" + 
				"		<div class=\"border_type\">\n" + 
				"			<h4 class=\"header\"><span class=\"blind\">글보내기 제한 공지</span></h4>\n" + 
				"			<div class=\"content\">\n" + 
				"				<dl class=\"cp_noti\" >\n" + 
				"					<dt><img src=\"https://blogimgs.pstatic.net/nblog/ico_notice2.gif\" width=\"29\" height=\"26\" alt=\"\"></dt>\n" + 
				"					<dd>저작권 침해가 우려되는 컨텐츠가 포함되어 있어<br/> 글보내기 기능을 제한합니다. </dd>\n" + 
				"				</dl>\n" + 
				"				<div class=\"cp_noti_desc\">\n" + 
				"					<p>네이버는 블로그를 통해 저작물이 무단으로 공유되는 것을 막기 위해,\n" + 
				"						저작권을 침해하는 컨텐츠가 포함되어 있는 게시물의 경우 글보내기 기능을 제한하고 있습니다.</p>\n" + 
				"					<p>상세한 안내를 받고 싶으신 경우 네이버 고객센터로 문의주시면 도움드리도록 하겠습니다.\n" + 
				"						건강한 인터넷 환경을 만들어 나갈 수 있도록 고객님의 많은 관심과 협조를 부탁드립니다.</p>\n" + 
				"				</div>\n" + 
				"			</div>\n" + 
				"			<a href=\"#BtnCLose\" class=\"closelayer\" onclick=\"$Element($('sendPostBlockLayerById')).hide();\"><img src=\"https://blogimgs.pstatic.net/nblog/book/publishingcompany/btn_close.gif\" alt=\"닫기\" width=\"15\" height=\"14\"></a>\n" + 
				"		</div>\n" + 
				"	</div></div></div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<div class=\"cp_layer_popup\" style=\"width:370px; display:none\" id=\"sendPostBlockModifyLayerById\">\n" + 
				"	<div class=\"shadow1\"><div class=\"shadow1_side\"><div class=\"shadow2\"><div class=\"shadow2_side\">\n" + 
				"		<div class=\"border_type\">\n" + 
				"			<h4 class=\"header\"><span class=\"blind\">주제 분류 제한 공지</span></h4>\n" + 
				"			<div class=\"content\">\n" + 
				"				<dl class=\"cp_noti\" >\n" + 
				"					<dt><img src=\"https://blogimgs.pstatic.net/nblog/ico_notice2.gif\" width=\"29\" height=\"26\" alt=\"\"></dt>\n" + 
				"					<dd>저작권 침해가 우려되는 컨텐츠가 포함되어 있어<br/> 주제 분류 기능을 제한합니다.</dd>\n" + 
				"				</dl>\n" + 
				"				<div class=\"cp_noti_desc\">\n" + 
				"					<p>네이버는 블로그를 통해 저작물이 무단으로 공유되는 것을 막기 위해,\n" + 
				"						저작권을 침해하는 컨텐츠가 포함되어 있는 게시물의 경우 주제 분류 기능을 제한하고 있습니다.</p>\n" + 
				"					<p>상세한 안내를 받고 싶으신 경우 네이버 고객센터로 문의주시면 도움드리도록 하겠습니다.\n" + 
				"						건강한 인터넷 환경을 만들어 나갈 수 있도록 고객님의 많은 관심과 협조를 부탁드립니다.</p>\n" + 
				"				</div>\n" + 
				"			</div>\n" + 
				"			<a href=\"#BtnCLose\" class=\"closelayer\" onclick=\"$Element($('sendPostBlockModifyLayerById')).hide();\"><img src=\"https://blogimgs.pstatic.net/nblog/book/publishingcompany/btn_close.gif\" alt=\"닫기\" width=\"15\" height=\"14\"></a>\n" + 
				"		</div>\n" + 
				"	</div></div></div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<form method=\"post\" action=\"https://blog.naverblogwidget.com/ExternalWidgetRender.naver?blogId=wood-24\" name=\"ExternalWidgetForm\">\n" + 
				"	<input type=\"hidden\" name=\"reversedWidgetCode\"/>\n" + 
				"	<input type=\"hidden\" name=\"blogId\"/>\n" + 
				"</form>\n" + 
				"\n" + 
				"\n" + 
				"<form name=\"loginForm\" method=\"post\" action=\"https://nid.naver.com/nidlogin.login?mode=form\">\n" + 
				"	<input type=\"hidden\" name=\"url\" value=\"\" />\n" + 
				"</form>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"c_layer_popup\" style=\"position:absolute;display:none;\">\n" + 
				"	<!--[if lt IE 7]>\n" + 
				"	<iframe width=\"1000px\" height=\"1000px\" style=\"overflow:hidden;position:absolute;top:0;left:0;\" frameborder=\"0\" title=\"빈프레임\"></iframe>\n" + 
				"	<![endif]-->\n" + 
				"	<p class=\"c_desc00\">작성하신 <strong><span>게시글</span>에 사용이 제한된 문구가 포함</strong>되어 일시적으로 <br /> 등록이 제한됩니다.</p>\n" + 
				"	<div class=\"c_contents\">\n" + 
				"		<p class=\"c_desc01\">이용자 분들이 홍보성 도배, 스팸 게시물로 불편을 겪지 않도록<br /> 다음과 같은 경우 해당 게시물 등록이 일시적으로 제한됩니다.</p>\n" + 
				"		<ul class=\"c_desc02\">\n" + 
				"			<li>특정 게시물 대량으로 등록되거나 해당 게시물에서 자주 사용하는<br />\n" + 
				"			  문구가 포함된 경우</li>\n" + 
				"			<li>특정 게시물이 과도하게 반복 작성되거나 해당 게시물에서 자주 사용하는<br />문구가 포함된 경우</li>\n" + 
				"		</ul>\n" + 
				"		<p class=\"c_desc03\">스팸 게시물이 확대 생성되는 것을 방지하기 위하여 문구 및 사용 제한기간을<br />상세하게 안내해 드리지 못하는 점 양해 부탁 드립니다. 모두가 행복한 인터넷<br />문화를 만들기 위한 네이버의 노력이오니 회원님의 양해와 협조 부탁드립니다.</p>\n" + 
				"		<p class=\"c_desc04\">더 궁금하신 사항은 <a href=\"https://help.naver.com/support/alias/blog/stats/stats1.naver\" target=\"_blank\">고객센터</a>로 문의하시면 자세히 알려드리겠습니다.</p><!-- //090708 -->\n" + 
				"		<p class=\"c_desc05\">수정하신 후 다시 등록해 주세요.</p>\n" + 
				"	</div>\n" + 
				"	<div class=\"c_footer\"><button type=\"button\" onclick=\"if(wordCheck) wordCheck.hideDialog();\">확인</button><button type=\"button\" class=\"c_close\" onclick=\"if(wordCheck) wordCheck.hideDialog();\"><img src=\"https://blogimgs.pstatic.net/static/common/popup/btn_close3.gif\" width=\"15\" height=\"14\" alt=\"닫기\" /></button></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"loginstart_layer\" class=\"layerpopup\" style=\"z-index:199;width:380px; top:100px; left:414px; display:none;\">\n" + 
				"	<div class=\"shadow1\"><div class=\"shadow1_side\">\n" + 
				"	<div class=\"shadow2\"><div class=\"shadow2_side\">\n" + 
				"	<div class=\"shadow3\"><div class=\"shadow3_side\">\n" + 
				"		<div class=\"border_type\">\n" + 
				"			<div class=\"logbox_wrap\">\n" + 
				"				<p class=\"notice\">회원님의 안전한 서비스 이용을 위해 <strong>비밀번호를 확인해 주세요.</strong></p>\n" + 
				"				<p class=\"notice02\">다시 한번 <strong>비밀번호 확인</strong> 하시면 이용중인 화면으로 돌아가며, 작성 중이던<br>내용을 정상적으로 전송 또는 등록하실 수 있습니다.</p>\n" + 
				"				<a href=\"#\" onClick=\"loginObj.closeLayeredLogin(); return false;\" class=\"closelayer\"><img src=\"https://blogimgs.pstatic.net/nblog/widget/btn_close.gif\" alt=\"닫기\" width=\"15\" height=\"14\"></a>\n" + 
				"				<div class=\"frame_wrap\"><iframe id=\"layeredframe\" src=\"about:blank\" width=\"350\" height=\"163\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" title=\"레이어용 프레임\"></iframe></div>\n" + 
				"			</div>\n" + 
				"		</div>\n" + 
				"	</div></div>\n" + 
				"	</div></div>\n" + 
				"	</div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"captcha_layer\" class=\"layerpopup\" style=\"z-index:-1;width:504px; top:0px; left:0px; display:none;\">\n" + 
				"	<input type=\"hidden\" id=\"target\" value=\"\"/>	<div class=\"frame_wrap\"><iframe id=\"captchalayeredframe\" src=\"about:blank\" width=\"504\" height=\"252\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\" title=\"안부게시판 캡차\"></iframe></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"day_write_limit_layer\"  class=\"layerpopup\" style=\"position:absolute;top:350px;left:50px;z-index:999;font-family:'돋움',dotum;font-size:12px;color:#666;line-height:16px; display:none;\">\n" + 
				"	<div style=\"width:306px;height:161px;padding:0 20px;border:2px solid #666;background:#fff\">\n" + 
				"		<div style=\"margin:24px 0 10px -1px;padding:0;color:#333\">\n" + 
				"			<strong>1일 안부글 작성횟수를 초과하셨습니다.</strong>\n" + 
				"		</div>\n" + 
				"		<div style=\"width:100%;margin-left:-1px\">\n" + 
				"			네이버 블로그에서는 프로그램을 이용한<br>\n" + 
				"			안부글 자동등록 방지를 위해 1일 안부글 작성횟수에<br>\n" + 
				"			제한을 두고 있습니다.\n" + 
				"		</div>\n" + 
				"		<div style=\"margin-top:16px;padding-top:9px;border-top:1px solid #e5e5e5;letter-spacing:-1px;text-align:center\">\n" + 
				"			<a href=\"#\" onClick=\"writeLimitObj.closeLayeredWriteLimit('day'); return false;\"  ><img src=\"https://blogimgs.pstatic.net/nblog/guestbook/btn_ok.gif\" width=\"47\" height=\"27\" alt=\"확인\" border=\"0\"  /></a>\n" + 
				"		</div>\n" + 
				"		<a href=\"#\" onClick=\"writeLimitObj.closeLayeredWriteLimit('day'); return false;\" class=\"closelayer\"><img src=\"https://blogimgs.pstatic.net/nblog/guestbook/btn_close2.gif\" width=\"14\" height=\"14\" alt=\"닫기\"  border=\"0\" /></a>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"reported_write_limit_layer\" class=\"layerpopup\" style=\"position:absolute;top:560px;left:50px;z-index:999;font-family:'돋움',dotum;font-size:12px;color:#666;line-height:16px; display:none;\">\n" + 
				"	<div style=\"width:341px;height:231px;padding:0 20px;border:2px solid #666;background:#fff\">\n" + 
				"		<div style=\"margin:24px 0 10px -1px;padding:0;color:#333\">\n" + 
				"			<strong>1일 안부글 작성횟수를 초과하셨습니다.</strong>\n" + 
				"		</div>\n" + 
				"		<div style=\"width:100%;margin-left:-1px\">\n" + 
				"			고객님이 남기신 안부글에 대한 다수의 신고가 접수되어<br>\n" + 
				"			1일 안부글 작성 횟수가 <span style=\"color:#2cae0c\"><strong>5회</strong>로 제한</span>되었습니다.\n" + 
				"			<p id=\"blockedDate\" style=\"margin:0;padding:7px 0 15px 1px\"></p>\n" + 
				"			네이버 블로그는 여러 사람이 함께 모여 즐거움을 나누는<br>\n" + 
				"			공간으로 모든 분들이 기분좋게 블로그를 이용할 수 있도록<br>\n" + 
				"			고객님의 이해와 협조 부탁 드립니다.\n" + 
				"		</div>\n" + 
				"		<div style=\"margin-top:16px;padding-top:9px;border-top:1px solid #e5e5e5;letter-spacing:-1px;text-align:center\">\n" + 
				"			<a href=\"#\" onclick=\"writeLimitObj.closeLayeredWriteLimit('reported'); return false;\" class=\"closeLayer\"><img src=\"https://blogimgs.pstatic.net/nblog/guestbook/btn_ok.gif\" width=\"47\" height=\"27\" alt=\"확인\"></a>\n" + 
				"		</div>\n" + 
				"		<a href=\"#\" onclick=\"writeLimitObj.closeLayeredWriteLimit('reported'); return false;\" class=\"closeLayer\" style=\"position:absolute;top:10px;right:10px\"><img src=\"https://blogimgs.pstatic.net/nblog/guestbook/btn_close2.gif\" width=\"19\" height=\"19\" alt=\"닫기\"></a>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"floatingda_content\" style=\"z-index:101; position:absolute; top:100px; right:10px;\" ></div>\n" + 
				"\n" + 
				"<script type=\"text/template\" id=\"tpl_video\">\n" + 
				"	<span class=\"ico_video\">동영상</span>\n" + 
				"</script>\n" + 
				"<script type=\"text/template\" id=\"tpl_recommend_buddy\">\n" + 
				"<strong class=\"tit\">{=recommendTitle}</strong>\n" + 
				"<div class=\"lyr_cont\">\n" + 
				"	<a href=\"/{=blogId}\" target=\"_blank\" onclick=\"nclk_v2(this,'bap.blog','','');\">\n" + 
				"		<div class=\"bloger\">\n" + 
				"			<span class=\"thumb\"><img src=\"{=profileImageUrl}\" width=\"35\" height=\"35\" alt=\"섬네일\"></span>\n" + 
				"			<span class=\"txt\">\n" + 
				"				<strong class=\"nick\">{=nickname}</strong>\n" + 
				"				<span class=\"dsc\">{=blogName}</span>\n" + 
				"			</span>\n" + 
				"		</div>\n" + 
				"	<a>\n" + 
				"	<a href=\"#\" class=\"btn_add_buddy _addBuddy _param({=blogId})\"><span class=\"ico\"></span>이웃추가</a>\n" + 
				"</div>\n" + 
				"<a href=\"#\" class=\"btn_close _closeLayer\">이웃추가 레이어 닫기</a>\n" + 
				"</script>\n" + 
				"\n" + 
				"\n" + 
				"<style type=\"text/css\">\n" + 
				"	.layerpop2{position:absolute; z-index:999; font:normal 12px 돋움,Dotum; color:#333; overflow:hidden; display:none;}\n" + 
				"	.layerpop2 .border_type{position:relative; border:solid 2px #777; background-color:#fff;}\n" + 
				"	.layerpop2 .close{position:absolute; top:8px; right:8px;}\n" + 
				"	.layerpop2 .content1{padding:30px 30px 21px; border-bottom:solid 1px #f0f0f0;}\n" + 
				"	.layerpop2 .content1 h6{font-weight:bold; font-size:14px; padding-bottom:10px; margin:0; color:#333;}\n" + 
				"	.layerpop2 .subtext{font-size:11px; color:#999; margin:0; padding:7px 0 0; line-height:16px; letter-spacing:-1px; text-align:left;}\n" + 
				"	.layerpop2 .check{width:13px; height:13px; vertical-align:middle; padding:0 3px 2px 0; margin-left:2px;}\n" + 
				"	.layerpop2 .btn1{background-color:#fbfbfb; border-top:solid 1px #f7f7f7; text-align:center; margin:0; padding:11px 0 20px;}\n" + 
				"	.layerpop2 .btn1 img{margin:0 1px; vertical-align:top;}\n" + 
				"	</style>\n" + 
				"<div id=\"sympathyDeleteLayer\" class=\"layerpop2\" style=\"background:#FFF; display:none; width:326px; top:200px; left:50px;\">\n" + 
				"	<div class=\"border_type\">\n" + 
				"		<div class=\"content1\">\n" + 
				"		<h6>공감을 삭제하시겠습니까?</h6>\n" + 
				"		<p class=\"subtext\">이 글의 공감수도 함께 차감됩니다.</p>\n" + 
				"		</div>\n" + 
				"		<p class=\"btn1\" id=\"sympathyLayer_deleteBtn\" style=\"padding:10px;\">\n" + 
				"			<a href=\"#\"><img src=\"https://blogimgs.pstatic.net/imgs/btn_confirm_pop2.gif\" width=\"55\" height=\"27\" alt=\"확인\" /></a><a href=\"#\" class=\"_deleteFormClose _returnFalse\"><img src=\"https://blogimgs.pstatic.net/imgs/btn_cancel_pop2.gif\" width=\"55\" height=\"27\" alt=\"취소\" /></a>\n" + 
				"		</p>\n" + 
				"		<a href=\"#\" class=\"close _deleteFormClose _returnFalse\"><img src=\"https://blogimgs.pstatic.net/static/ws/btn_close.gif\" alt=\"닫기\" width=\"15\" height=\"14\"></a>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"<div id=\"buddyRecommendLayer\" class=\"pop_buddy_add_lyr\" style=\"display:none;\">\n" + 
				"	 <strong class=\"tit\">이웃으로 추가하시겠어요?</strong>\n" + 
				"	 <div class=\"lyr_cont\">\n" + 
				"	         <div class=\"bloger\">\n" + 
				"	           <span class=\"thumb\">\n" + 
				"	           \n" + 
				"	           	\n" + 
				"	           	\n" + 
				"	           		<img src=\"https://blogpfthumb-phinf.pstatic.net/MjAxODExMTBfODcg/MDAxNTQxODMzMjg0NzUy.D64-EQ1hrmaSPKFGT0jSt5t4EK7cFn1lZnbf-rwsSg8g.D5Dss_alDxNrB-aYWxnI0FBtVTMPuFwY-58EexTl2uQg.JPEG.wood-24/%BB%F3%C8%A3.jpg?type=s40\" width=\"35\" height=\"35\" alt=\"프로필\" onerror=\"this.onerror=null; this.src='https://blogimgs.pstatic.net/nblog/comment/login_basic.gif'\">\n" + 
				"	    	   	\n" + 
				"	    	   \n" + 
				"	           </span>\n" + 
				"	           <span class=\"txt\">\n" + 
				"	                   <strong class=\"nick\">우드24</strong>\n" + 
				"	                   <span class=\"dsc\">우드24</span>\n" + 
				"	           </span>\n" + 
				"	         </div>\n" + 
				"	         <a href=\"javascript:void(0)\" class=\"btn_add_buddy _addBuddy _param(wood-24)\"><span class=\"ico\"></span>이웃추가</a>\n" + 
				"	         \n" + 
				"	 </div>\n" + 
				"	 <a href=\"#\" class=\"btn_close _buddyRecommendCloseLayer\">이웃추가 레이어 닫기</a>\n" + 
				"</div>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"<style type=\"text/css\">\n" + 
				"/* Default Type Selector */\n" + 
				"#banword_wrap *, #banword_wrap1 *{ margin:0; padding:0; font-size:12px; font-family:돋움, Dotum, 굴림, Gulim, Helvetica, Sans-serif; text-align:left;}\n" + 
				"#banword_wrap a, #banword_wrap1 a{ color:#004790;}\n" + 
				"#banword_wrap img, #banword_wrap1 img,\n" + 
				"#banword_wrap fieldset, #banword_wrap1 fieldset{ border:none;}\n" + 
				"#banword_wrap legend, #banword_wrap1 legend{ display:none;}\n" + 
				"#banword_wrap em, #banword_wrap1 em{ font-style:normal; color:#258102;}\n" + 
				"#banword_wrap strong, #banword_wrap1 strong{ color:#258102;}\n" + 
				"#banword_wrap li, #banword_wrap1 li{ list-style:none;}\n" + 
				"#banword_wrap h1, #banword_wrap1 h1{ font:bold 13px 굴림, Gulim, 돋움, Dotum, Helvetica, Sans-serif; color:#333; padding:11px 0 0 19px; *padding:12px 0 0 19px; }\n" + 
				"/* Layout */\n" + 
				"#banword_wrap{ position:absolute; width:410px; _width /**/:418px; overflow:hidden; border:4px solid #777; background:#fff; display: none; z-index: 200;}\n" + 
				"#banword_wrap1{ position:relative; width:410px; _width /**/:418px;height:446px; overflow:hidden; border:4px solid #777; background:#fff;}\n" + 
				"#bw_content{ padding:14px 0 28px 0; margin:0 19px 0 18px;}\n" + 
				"#bw_footer{ height:8px; _height:8px; text-align:center;}\n" + 
				"body:first-of-type #bw_footer{ height:7px;}\n" + 
				"#bw_footer, x:-moz-any-link, x:default { height:7px;}\n" + 
				"*:first-child+html #bw_footer{ height:8px;}\n" + 
				"#bw_btn_footer{ height:46px; _height /**/:45px; text-align:center; background:url(https://ssl.pstatic.net/static/common/popup/bg_not_available_common.gif) no-repeat left 1px; padding:16px 0 0 0 ; _padding /**/:16px 0 20px 0 ;}\n" + 
				"\n" + 
				"/* Common */\n" + 
				"#bw_content .bw_bx1{border:1px solid #f1f1f1; margin:0 1px 9px 2px; height:100px; background:#f8f8f8; }\n" + 
				"#bw_content .bw_bx1_shadow{position:relative;  padding:13px 13px 12px 0; }\n" + 
				"#bw_content .bw_bx1_shadow strong { color:#282828; vertical-align:top; margin-left:13px; margin-bottom:10px; display:block; }\n" + 
				"#bw_content .bw_bx1_shadow a img {vertical-align:middle; margin:-1px 0 0 0; *margin:-2px 0 0 0;}\n" + 
				"@media all and (-webkit-min-device-pixel-ratio:10000), not all and (-webkit-min-device-pixel-ratio:0)\n" + 
				"{\n" + 
				"	#bw_content .bw_bx1_shadow a img {margin:-1px !important;}\n" + 
				"}\n" + 
				"body:first-of-type #bw_content .bw_bx1_shadow a img {vertical-align:middle; margin:-5px 0 0 0;}\n" + 
				"#bw_content .bw_bx1_shadow_in {height:35px; _height /**/:53px; margin:7px 0 0 13px; _margin:5px 0 0 13px; padding:8px 13px 7px 12px; _padding:9px 13px 8px 12px; border:#e3e3e3 1px solid; color:#666; line-height:20px; background:#fff; overflow:auto;}\n" + 
				"body:first-of-type #bw_content .bw_bx1_shadow_in {height:35px; _height /**/:53px; margin:5px 0 0 13px; padding:8px 13px 9px 12px; border:#e3e3e3 1px solid; color:#666; line-height:20px; background:#fff; overflow:auto;}\n" + 
				"#bw_content .bw_bx1_shadow_in, x:-moz-any-link, x:default {height:35px; _height /**/:53px; margin:5px 0 0 13px; padding:8px 13px 9px 12px; border:#e3e3e3 1px solid; color:#666; line-height:20px; background:#fff; overflow:auto;}\n" + 
				"*:first-child+html #bw_content .bw_bx1_shadow_in {height:33px; _height /**/:53px; margin:7px 0 0 13px; padding:8px 13px 9px 12px; border:#e3e3e3 1px solid; color:#666; line-height:20px; background:#fff; overflow:auto;}\n" + 
				"\n" + 
				"#bw_content .bw_bx2{ border:1px solid #eeeeee; margin:0 0 6px 3px; background:#fbfbfb; line-height:18px;}\n" + 
				"#bw_content .bw_bx2 *{ color:#444444;}\n" + 
				"/* Content */\n" + 
				"#bw_content .bw_desc1{ color:#444444; font-weight:bold; margin:0 0 4px 0;  letter-spacing:-1px;}\n" + 
				"#bw_content .bw_desc2{ color:#444444; margin:0 0 13px 0;}\n" + 
				"\n" + 
				"#bw_content .bw_desc3{ position:relative; color:#696968; padding:9px 2px 9px 34px; font-size:11px; letter-spacing:-1px; background:url(https://ssl.pstatic.net/static/common/popup/bu_attention.gif) no-repeat 13px 12px;}\n" + 
				"body:first-of-type #bw_content .bw_desc3{ padding:10px 2px 8px 34px;}\n" + 
				"#bw_content .bw_desc3, x:-moz-any-link, x:default { padding:10px 2px 8px 34px;}\n" + 
				"*:first-child+html #bw_content .bw_desc3{ padding:9px 2px 9px 34px;}\n" + 
				"\n" + 
				"#bw_content .bw_desc4 { color:#6c6c6c; margin:10px 0 0 33px; font-size:11px; letter-spacing:-1px;}\n" + 
				"body:first-of-type #bw_content .bw_desc4 { margin:9px 0 0 33px;}\n" + 
				"#bw_content .bw_desc4, x:-moz-any-link, x:default { margin:9px 0 0 33px;}\n" + 
				"*:first-child+html #bw_content .bw_desc4 { margin:10px 0 0 33px;}\n" + 
				"#bw_content .bw_desc4 a {color:#258102; font-size:11px;}\n" + 
				"\n" + 
				"#bw_content .bw_desc5 { color:#333333; margin:16px 0 10px 0; _margin:16px 0 10px 0; padding:0 0 0 1px; letter-spacing:-1px;}\n" + 
				"body:first-of-type #bw_content .bw_desc5 { color:#333333; margin:14px 0 12px 0; padding:0 0 0 1px; letter-spacing:-1px;}\n" + 
				"#bw_content .bw_desc5, x:-moz-any-link, x:default { color:#333333; margin:14px 0 12px 0; padding:0 0 0 1px; letter-spacing:-1px;}\n" + 
				"*:first-child+html #bw_content .bw_desc5 { color:#333333; margin:16px 0 12px 0; _margin:16px 0 10px 0; padding:0 0 0 1px; letter-spacing:-1px;}\n" + 
				"#bw_content .bw_desc5 strong{ color:#333333;}\n" + 
				"\n" + 
				"#bw_content .bw_desc6{color:#333333; margin:21px 0 0 0; padding:0 0 5px 1px; _padding:0 0 3px 1px; letter-spacing:-1px; border-bottom:1px solid #ececec; line-height:20px;}\n" + 
				"/*html:first-child #bw_content .bw_desc6 {margin:46px 0 0 0; padding:0 0 8px 1px;}\n" + 
				"body:first-of-type #bw_content .bw_desc6 {margin:26px 0 0 0; padding:0 0 8px 1px;}\n" + 
				"#bw_content .bw_desc6, x:-moz-any-link, x:default {margin:26px 0 0 0; padding:0 0 8px 1px;}\n" + 
				"*:first-child+html #bw_content .bw_desc6 { margin:27px 0 0 0; padding:0 0 9px 1px;}*/\n" + 
				"#bw_content .bw_desc6 strong{ color:#333333;}\n" + 
				"\n" + 
				"#bw_content .layer_content p {font-size:11px; line-height:14px; color:#717171; letter-spacing:-1px;}\n" + 
				"#bw_content .layer_content p.bw_orange {margin-top:5px; color:#fd630a;}\n" + 
				"/* function button box */\n" + 
				"#bw_content .function_btn {overflow:hidden; height:53px; *height:57px; _height:60px; width:362px; margin:14px 0 21px 6px; padding-top:4px;}\n" + 
				"body:first-of-type #bw_content .function_btn { margin:14px 0 20px 6px;}\n" + 
				"#bw_content .function_btn, x:-moz-any-link, x:default  { margin:14px 0 20px 6px;}\n" + 
				"*:first-child+html #bw_content .function_btn { margin:14px 0 21px 6px;}\n" + 
				"\n" + 
				"#bw_content .function_btn .btn_wrap {float:left; width:174px; _width /**/:181px; padding:0 0 0 7px;}\n" + 
				"#bw_content .function_btn .btn_wrap p {margin-top:5px; font-size:11px; letter-spacing:-1px; color:#696968; background:url(https://ssl.pstatic.net/static/common/popup/bu_line.gif) no-repeat right top;}\n" + 
				"body:first-of-type #bw_content .function_btn .btn_wrap p {margin-top:6px;}\n" + 
				"#bw_content .function_btn .btn_wrap p, x:-moz-any-link, x:default  {margin-top:6px;}\n" + 
				"*:first-child+html #bw_content .function_btn .btn_wrap p {margin-top:5px;}\n" + 
				"\n" + 
				"#bw_content .function_btn .btn_wrap1 {float:left; width:179px; _width /**/:181px; text-align:center; padding-left:2px}\n" + 
				"#bw_content .function_btn .btn_wrap1 p {margin-top:5px; font-size:11px; letter-spacing:-1px; color:#696968; text-align:left; padding:0 0 0 4px;}\n" + 
				"body:first-of-type #bw_content .function_btn .btn_wrap1 p {margin-top:6px;}\n" + 
				"#bw_content .function_btn .btn_wrap1 p, x:-moz-any-link, x:default  {margin-top:6px;}\n" + 
				"*:first-child+html #bw_content .function_btn .btn_wrap1 p {margin-top:5px;}\n" + 
				"\n" + 
				"#bw_footer button.c_close{position:absolute; top:7px; right:6px; width:18px; height:17px; background:none; border:none;}\n" + 
				"#bw_footer button.c_close, x:-moz-any-link, x:default {top:5px; right:8px;}\n" + 
				"*:first-child+html #bw_footer button.c_close {top:7px; right:6px;}\n" + 
				"\n" + 
				"#bw_btn_footer button {width:55px; height:27px; text-align:center; font-size:12px;}\n" + 
				"@media all and (-webkit-min-device-pixel-ratio:10000), not all and (-webkit-min-device-pixel-ratio:0)\n" + 
				"{\n" + 
				"	#bw_btn_footer button {padding-top:6px;}\n" + 
				"}\n" + 
				"#bw_btn_footer button.c_close{position:absolute; top:7px; right:6px; width:18px; height:17px; background:none; border:none;}\n" + 
				"#bw_btn_footer button.c_close, x:-moz-any-link, x:default {top:5px; right:8px;}\n" + 
				"*:first-child+html #bw_btn_footer button.c_close {top:7px; right:6px;}\n" + 
				"\n" + 
				"</style>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"	var wordCheck;\n" + 
				"	function setWordCheckObject(obj) {\n" + 
				"		wordCheck = obj;\n" + 
				"	}\n" + 
				"</script>\n" + 
				"	<!-- 레이어팝업2 Document Size : 410*372 -->\n" + 
				"<div id=\"banword_wrap\">\n" + 
				"	<div id=\"bw_content\">\n" + 
				"		<p class=\"bw_desc5\">작성하신 <strong><span id=\"bw_ugc_type\"></span>에 이용자들의 신고가 많은 표현이 포함</strong>되어 있습니다.</p>\n" + 
				"		<div class=\"bw_bx1\">\n" + 
				"			<div class=\"bw_bx1_shadow\">\n" + 
				"				<strong>신고가 많은 표현</strong>\n" + 
				"				<div id='bw_ban_words' class=\"bw_bx1_shadow_in\">\n" + 
				"				</div>\n" + 
				"			</div>\n" + 
				"		</div>\n" + 
				"		<div class=\"bw_bx2\">\n" + 
				"			<p class=\"bw_desc3\">다른 표현을 사용해주시기 바랍니다.<br>\n" + 
				"			건전한 인터넷 문화 조성을 위해 회원님의 적극적인 협조를 부탁드립니다.</p>\n" + 
				"		</div>\n" + 
				"		<p class=\"bw_desc4\">더 궁금하신 사항은 <a href=\"https://help.naver.com/support/alias/blog/stats/stats1.naver\" target=\"_blank\">고객센터</a>로 문의하시면 자세히 알려드리겠습니다.</p>\n" + 
				"	</div>\n" + 
				"		<div id=\"bw_btn_footer\">\n" + 
				"		<button type=\"button\" onclick=\"if(wordCheck) wordCheck.hideDialog();\">확인</button>\n" + 
				"		<button type=\"button\" class=\"c_close\" onclick=\"if(wordCheck) wordCheck.hideDialog();\"><img src=\"https://blogimgs.pstatic.net/static/common/popup/btn_close3.gif\" width=\"15\" height=\"14\" alt=\"닫기\"></button>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"	<div id=\"dimmed_layer\" style=\"z-index:198; position:absolute;left:0px; top:0px;  background: #000; opacity:0.1;filter:alpha(opacity=10); display:none;\"></div>\n" + 
				"	<div id=\"dimmed_iframe_layer\" style=\"z-index:197; position:absolute;left:0px; top:0px;  background: #000; opacity:0.1;filter:alpha(opacity=10); display:none;\"><iframe  style=\"z-index:198; width: 100%; height: 100%; position:absolute;left:0px; top:0px;  background: #000; opacity:0;filter:alpha(opacity=0);\" title=\"ie6에서 사용되는 팝업시 비활성화 프레임\"></iframe></div>\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"if (!window.nhn) window.nhn = {};\n" + 
				"if (!window.nhn.Blog) window.nhn.Blog = {};\n" + 
				"if (!window.nhn.Blog.SocialApp) window.nhn.Blog.SocialApp = {};\n" + 
				"\n" + 
				"var gsNDriveUrl = \"https://mybox.naver.com\";\n" + 
				"var gsStaticNetUrl = \"https://ssl.pstatic.net/static\";\n" + 
				"</script>\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning/LayoutBottomCommon-165671165_https.js\" charset=\"UTF-8\"></script>\n" + 
				"\n" + 
				"\n" + 
				"	<script type=\"text/javascript\" src=\"https://ssl.pstatic.net/t.static.blog/mylog/versioning//common/js/mylog/floating/FloatingBarAreaHandler-6728c51_https.js\"></script>\n" + 
				"\n" + 
				"\n" + 
				"<script type=\"text/javascript\">\n" + 
				"function openLoginPopup() {\n" + 
				"	var closeUrl = blogURL + \"/post/common/reloadOpenerParentAndClose.jsp\";\n" + 
				"	window.open(idURLSecret + \"/nidlogin.login?mode=form&template=plogin&url=\"+closeUrl,\"loginPopup\",\"width=400,height=267\");\n" + 
				"	return;\n" + 
				"}\n" + 
				"\n" + 
				"var blogId = 'wood-24';\n" + 
				"</script>\n" + 
				"<script type=\"text/javascript\">\n" + 
				"if ( typeof g_ssc == 'undefined') {\n" + 
				"	g_ssc = \"blog.post\";\n" + 
				"}\n" + 
				"\n" + 
				"var ccsrv=\"cc.naver.com\";\n" + 
				"var nclk_evt = 3;\n" + 
				"\n" + 
				"var bIsNotLoginUser = \"true\" === \"true\";\n" + 
				"var sIdSecretUrl = 'https://nid.naver.com';\n" + 
				"\n" + 
				"\n" + 
				"</script>\n" + 
				"\n" + 
				"<div id=\"marketWelcomeLayerTemp\" style=\"display: none\">\n" + 
				"	<div id=\"blogMarketWelcomeLayer\" class=\"blog_market_bridge_set_delivery_popup\">\n" + 
				"			<div class=\"dimmed\"></div>\n" + 
				"			<div class=\"blog_market_bridge_set_delivery_content\">\n" + 
				"			<div class=\"text_area\">\n" + 
				"			<h3 class=\"title\">블로그 마켓 가입 완료</h3>\n" + 
				"	<p class=\"desc\">내 상품 관리에서 배송비 설정 후 <br>상품 판매를 시작해보세요!</p>\n" + 
				"	</div>\n" + 
				"	<div class=\"btn_area\">\n" + 
				"			<a href=\"#\" class=\"btn\" onclick=\"blogMarketWelcomeController.goDeliveryFeeSetPage('https://seller.blog.naver.com')\">배송비 설정하기</a>\n" + 
				"	</div>\n" + 
				"	<button class=\"close_btn\" onclick=\"blogMarketWelcomeController.hideWelcomeLayer()\"><span class=\"blind\">블로그 마켓 가입 완료 레이어 닫기</span></button>\n" + 
				"	</div>\n" + 
				"	</div>\n" + 
				"	<style>\n" + 
				"	.blog_market_bridge_set_delivery_popup{position:fixed;left:0;top:0;right:0;bottom:0;z-index:10000;font-size:0;text-align:center;}\n" + 
				"	.blog_market_bridge_set_delivery_popup:before{display:inline-block;width:0;height:100%;vertical-align:middle;content:'';}\n" + 
				"	.blog_market_bridge_set_delivery_popup .dimmed{position:absolute;left:0;top:0;right:0;bottom:0;opacity: 0.7;background: #000000;}\n" + 
				"	.blog_market_bridge_set_delivery_popup .blog_market_bridge_set_delivery_content{display:inline-block;position:relative;width:304px;height:303px;border-radius:8px;box-shadow:1px 1px 7px 0 rgba(0, 0, 0, 0.15);border:solid 1px rgba(0, 0, 0, 0.1);background:#fff;text-align:center;vertical-align:middle;}\n" + 
				"	.blog_market_bridge_set_delivery_popup .text_area:before{\n" + 
				"		display:inline-block;width:192px;height:102px;margin:12px 0 0;content:'';vertical-align:top;\n" + 
				"		background: url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' width='192' height='102' viewBox='0 0 192 102'%3E%3Cdefs%3E%3ClinearGradient id='e8xiwmcq5a' x1='50%25' x2='50%25' y1='50%25' y2='100%25'%3E%3Cstop offset='0%25' stop-color='%2338D77C'/%3E%3Cstop offset='100%25' stop-color='%232FCFDC'/%3E%3C/linearGradient%3E%3Cfilter id='2o3xn9qpnb' width='125%25' height='125%25' x='-12.5%25' y='-12.5%25' filterUnits='objectBoundingBox'%3E%3CfeOffset in='SourceAlpha' result='shadowOffsetOuter1'/%3E%3CfeGaussianBlur in='shadowOffsetOuter1' result='shadowBlurOuter1' stdDeviation='1'/%3E%3CfeColorMatrix in='shadowBlurOuter1' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.210500437 0'/%3E%3C/filter%3E%3Ccircle id='2pb8blndrc' cx='12' cy='12' r='12'/%3E%3C/defs%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg%3E%3Cg%3E%3Cg%3E%3Cg transform='translate(-624 -259) translate(568 247) translate(56 12) translate(10 2)'%3E%3Ccircle cx='32.918' cy='96.666' r='2' fill='%23FF7D9F'/%3E%3Ccircle cx='142.418' cy='45.166' r='2.5' fill='%23FFD33E'/%3E%3Ccircle cx='66.918' cy='17.666' r='2' fill='%23928FF4'/%3E%3Ccircle cx='158.918' cy='57.666' r='2' fill='%237CD9B1'/%3E%3Ccircle cx='14.918' cy='14.666' r='4' fill='%23FFD33E'/%3E%3Cpath fill='%23928FF4' d='M2.207 54.71c.985.773 2.167 1.16 3.546 1.16 1.38 0 2.81-.43 4.29-1.288-.777 1.79-1.166 3.26-1.166 4.412 0 1.151.346 2.293 1.039 3.424-.93-.648-2.093-.972-3.491-.972-1.399 0-2.709.228-3.93.685.537-1.33.806-2.599.806-3.809s-.365-2.414-1.094-3.612z' transform='rotate(-45 6.126 58.5)'/%3E%3Crect width='9' height='3' x='142.918' y='90.666' fill='%23FF7D9F' rx='1.5' transform='rotate(43 147.418 92.166)'/%3E%3Crect width='9' height='3' x='23.918' y='79.666' fill='%23FFD33E' rx='1.5' transform='rotate(-41 28.418 81.166)'/%3E%3Crect width='9' height='3' x='123.918' y='17.666' fill='%23928FF4' rx='1.5' transform='rotate(-41 128.418 19.166)'/%3E%3Crect width='9' height='3' x='27.918' y='30.666' fill='%237CD9B1' rx='1.5' transform='rotate(43 32.418 32.166)'/%3E%3Cpath fill='%23FF7D9F' d='M152 20.194c.86.674 1.89 1.012 3.094 1.012 1.203 0 2.45-.375 3.743-1.124-.679 1.561-1.018 2.844-1.018 3.849 0 1.004.302 2 .906 2.987-.81-.565-1.826-.848-3.046-.848-1.22 0-2.363.199-3.428.597.469-1.159.703-2.266.703-3.322s-.318-2.106-.954-3.151z' transform='rotate(-45 155.418 23.5)'/%3E%3Cpath fill='%237CD9B1' d='M66.389 2.115c.88.69 1.936 1.036 3.167 1.036 1.232 0 2.51-.384 3.833-1.151-.695 1.599-1.043 2.912-1.043 3.94 0 1.03.31 2.049.928 3.06-.83-.58-1.869-.869-3.118-.869-1.25 0-2.42.204-3.51.612.48-1.187.72-2.32.72-3.402 0-1.08-.326-2.156-.977-3.226z' transform='rotate(-45 69.889 5.5)'/%3E%3C/g%3E%3Cg%3E%3Cpath fill='url(%23e8xiwmcq5a)' d='M39 0c6.443 0 11.667 5.223 11.667 11.667l-.001 1.286H57c4.418 0 8 3.57 8 7.975v26.801c0 7.026-5.047 12.721-12.095 12.721H25.761C18.714 60.45 13 54.755 13 47.73V20.927c0-4.404 3.582-7.974 8-7.974l6.333-.001v-1.286C27.333 5.223 32.557 0 39 0zm-3.386 23.321h-5.947v26.025h5.948V47.51c1.308 1.484 3.251 2.382 5.908 2.382 5.195 0 9.477-4.49 9.477-10.306 0-5.816-4.282-10.305-9.477-10.305-2.541 0-4.43.821-5.734 2.19l-.174.19v-8.34zm4.72 11.113c2.96 0 5.106 1.985 5.106 5.153 0 3.168-2.146 5.153-5.106 5.153-2.961 0-5.106-1.985-5.106-5.153 0-3.168 2.145-5.153 5.106-5.153zM39 4.914c-3.72 0-6.752 2.953-6.876 6.643l-.004.237-.001 1.159H45.88v-1.16c0-3.799-3.08-6.88-6.88-6.88z' transform='translate(-624 -259) translate(568 247) translate(56 12) translate(54.5 29)'/%3E%3Cg%3E%3Cg transform='translate(-624 -259) translate(568 247) translate(56 12) translate(54.5 29) translate(53 43)'%3E%3Cuse fill='%23000' filter='url(%232o3xn9qpnb)' xlink:href='%232pb8blndrc'/%3E%3Cuse fill='%23FFF' xlink:href='%232pb8blndrc'/%3E%3C/g%3E%3Cpath fill='%2303C75A' d='M16.238 16.83H8.61v-2.034h5.594V4.029h2.034V16.83z' transform='translate(-624 -259) translate(568 247) translate(56 12) translate(54.5 29) translate(53 43) rotate(45 12.424 10.43)'/%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/svg%3E%0A\") no-repeat;\n" + 
				"	}\n" + 
				"	.blog_market_bridge_set_delivery_popup .title{font-family:\"나눔스퀘어\", \"NanumSquare\", \"NanumSquareWebFont\", \"Malgun Gothic\", \"맑은 고딕\", \"Dotum\", \"돋움\", \"Helvetica\", \"Apple SD Gothic Neo\", \"sans-serif\";font-size:22px;font-weight:bold;line-height:24px;color:#000;padding:12px 0 0;}\n" + 
				"	.blog_market_bridge_set_delivery_popup .desc{font-size:14px;line-height:23px;color:#000;padding:12px 0 0;}\n" + 
				"	.blog_market_bridge_set_delivery_popup .btn{display:block;height:40px;margin:25px 30px 0;background:#03c75a;border-radius:4px;line-height:41px;color:#fff;font-size:15px;font-weight:600;}\n" + 
				"	.blog_market_bridge_set_delivery_popup .btn:hover{text-decoration:none;}\n" + 
				"	.blog_market_bridge_set_delivery_popup .close_btn{position:absolute;right:-7px;top:-50px;width:44px;height:44px;background:url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='30' height='30' viewBox='0 0 30 30'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23FFF'%3E%3Cg%3E%3Cpath d='M22.5 7v14.5H37v1H22.499L22.5 37h-1l-.001-14.5H7v-1h14.5V7h1z' transform='translate(-842 -205) translate(835 198) rotate(45 22 22)'/%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/svg%3E%0A\") 7px 7px no-repeat;border:0;}\n" + 
				"	</style>\n" + 
				"</div>\n" + 
				"\n" + 
				"<div style=\"display: none;\" id=\"blogMarketSecedeLayer\" >\n" + 
				"	<div class=\"blog__market_leave_popup\">\n" + 
				"		<div class=\"dimmed\"></div>\n" + 
				"		<div class=\"blog__market_leave_layer\">\n" + 
				"			<div class=\"blog__market_leave_layer_wrap\" tabindex=\"-1\">\n" + 
				"				<div class=\"blog__market_leave_content\">\n" + 
				"					<div class=\"title\">블로그 마켓 탈퇴가<br>완료되었습니다.</div>\n" + 
				"					<div class=\"text\">문의사항은 블로그 마켓 고객센터로 연락주세요.</div>\n" + 
				"					<div class=\"btn_area\">\n" + 
				"						<button type=\"button\" class=\"btn_ok\" onclick=\"blogMarketSecedeController.hideSecedeLayer()\">확인</button>\n" + 
				"					</div>\n" + 
				"				</div>\n" + 
				"			</div>\n" + 
				"		</div>\n" + 
				"	</div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<style>\n" + 
				"	.blog__market_leave_popup{position:fixed;left:0;top:0;right:0;bottom:0;z-index:10000;font-size:0;text-align:center;}\n" + 
				"	.blog__market_leave_popup:before{display:inline-block;width:0;height:100%;vertical-align:middle;content:'';}\n" + 
				"	.blog__market_leave_popup .dimmed{position:absolute;left:0;top:0;right:0;bottom:0;opacity: 0.7;background: #000000;}\n" + 
				"	.blog__market_leave_popup .blog__market_leave_layer{display: inline-block;vertical-align: middle;white-space: normal;font-size: 12px;text-align: left;position: relative;outline: none;}\n" + 
				"	.blog__market_leave_popup .blog__market_leave_layer_wrap {box-sizing: border-box;width: 380px;padding: 0 17px;border-radius: 8px;box-shadow: 1px 1px 7px 0 rgba(0, 0, 0, 0.15);border: 1px solid rgba(0, 0, 0, 0.1);background-color: #fff;text-align: center;}\n" + 
				"	.blog__market_leave_popup .blog__market_leave_content {padding: 40px 0 33px;font-family:\"나눔스퀘어\", \"NanumSquare\", \"NanumSquareWebFont\", \"Malgun Gothic\", \"맑은 고딕\", \"Dotum\", \"돋움\", \"Helvetica\", \"Apple SD Gothic Neo\", \"sans-serif\";}\n" + 
				"	.blog__market_leave_popup .title{font-weight: 700;line-height: 34px;font-size: 24px;color: #444;}\n" + 
				"	.blog__market_leave_popup .text{margin-top: 10px;padding: 0 20px;word-break: keep-all;line-height: 21px;font-size: 13px;color: #888;}\n" + 
				"	.blog__market_leave_popup .btn_area{margin-top: 27px;font-size: 0;}\n" + 
				"	.blog__market_leave_popup .btn_ok{display: inline-block;vertical-align:top;width: 120px;height: 40px;border-radius: 3px;border: 1px solid #ddd;line-height: 14px;font-size: 14px;outline: 0;background-color: transparent;color: #00c73c;}\n" + 
				"	.blog__market_leave_popup .btn_ok:hover{color: #fff;background-color: #00c73c;border-color: #00c73c;}\n" + 
				"</style>\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"\n" + 
				"</body>\n" + 
				"</html>";
		
		return new ResponseEntity<String>(content, HttpStatus.OK);
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}