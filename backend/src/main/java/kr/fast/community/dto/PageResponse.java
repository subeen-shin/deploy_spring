package kr.fast.community.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

//페이지 처리를 위한 클래스를 제네릭으로 생성
//게시글, 댓글 등에서 다양하게 활용할 수 있기 때문에
@Getter
public class PageResponse<T> {

	private List<T> content; //컨텐츠 목록
	
	private int page; //현재 페이지 번호. 1부터 시작
	private int pageSize; //한 페이지의 컨텐츠 수 
	private long totalContentSize; //전체 컨텐츠 수(전체 = 검색적용된)
	private int totalPages; //전체 페이지수
	
	//페이지네이션 정보
	private int startPage; //페이지네이션의 시작 번호
	private int endPage; //페이지네이션의 마지막 번호
	private boolean hasPrev; //이전 버튼 활성화 여부
	private boolean hasNext;//다음 버튼 활성화 여부
	private int pageBlockSize;//페이지네이션에서 보여줄 페이지 개수
	
	public PageResponse(Page<T> page, int pageBlockSize) {
		this.pageBlockSize = pageBlockSize;
		
		this.content = page.getContent();
		
		//서버에서 가져올 땐 0페이지부터 시작하고 화면에선 1페이지부터 시작
		//this.page는 화면 페이지, page.getNumber()는 서버에서 검색할 때 사용한 페이지
		this.page = page.getNumber() + 1;
		this.totalContentSize = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		
		//마지막페이지 계산
		//1~10페이지를 표현하고 싶음. 현재 3페이지
		// 3.0/10 =>0.3 올림=> 1 => 곱하기 10 => 10페이지
		this.endPage =
			(int) (Math.ceil((double) this.page / pageBlockSize)) * pageBlockSize;
		this.startPage = endPage - pageBlockSize + 1;
		
		//마지막 페이지 번호가 전체 게시글 기준으로 마지막 페이지보다 크면 안되기 때문에 다음 작업을 함
		//예 : 현재 11페이지, 위에서 계산한 마지막 페이지 20. 실제 컨텐츠 수로인한 마지막 페이지는 16페이지
		
		if(totalPages < endPage) {
			this.endPage = totalPages;
		}
		
		//컨텐츠가 없는 경우
		if(endPage == 0) {
			endPage = 1;
		}
		
		//이전 버튼 활성화
		this.hasPrev = this.startPage > 1;
		//다음 버튼 활성화
		this.hasNext = this.endPage < this.totalPages;
 	}
}
