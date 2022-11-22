package com.gaia.util;

public class PageUtils {

		//当前页
		private int currentPage;
		//上一页
		private int prevPage;
		//下一页
		private int nextPage;
		//尾页
		private int lastPage;
		//数据总条数
		private int count;
		//每页条数
		private int  pageSize;
		//分页计入数
		private int pageRecord;
		//分页
		private String page;
		
		
		public PageUtils(String currentPage,int count,int pageSize) {
			init(currentPage, count, pageSize);
			initPrevPage();
			initLastPage();
			initNextPage();
			initPageRecord();
			initPage();
		}
		//初始化变量的值
		private void init(String currentPage,int count,int pageSize) {
			if(currentPage==null||currentPage.equals("")) {
				currentPage="1";
			}
			this.currentPage = Integer.parseInt(currentPage);
			this.count = count;
			this.pageSize = pageSize;
		}
		//计算上一页
		private void initPrevPage() {
			if(currentPage==1) {
				prevPage = 1;
			}else {
				prevPage = currentPage-1;
			}
		}
		//计算最后一页
		private void initLastPage() {
			if(count%pageSize==0) {
				lastPage=count/pageSize;
			}else {
				lastPage=count/pageSize+1;
			}
		}
		//计算下一页
		private void initNextPage() {
			if(currentPage==lastPage) {
				nextPage=lastPage;
			}else {
				nextPage=currentPage+1;
			}
		}
		//计算计入数
		private void initPageRecord() {
			pageRecord = (currentPage-1)*pageSize;
		}
		
		
		private void initPage() {
			page = "第"+currentPage+"/"+lastPage+"页，共"+count+"条数据。";
			page +="<input type='button' value='首页' onclick='page(1)' >";
			page +="<input type='button' value='上一页' onclick='page("+prevPage+")' >";
			page +="<input type='button' value='下一页' onclick='page("+nextPage+")' >";
			page +="<input type='button' value='尾页' onclick='page("+lastPage+")' >";
		}

		public int getCurrentPage() {
			return currentPage;
		}


		public int getPrevPage() {
			return prevPage;
		}


		public int getNextPage() {
			return nextPage;
		}


		public int getLastPage() {
			return lastPage;
		}


		public int getCount() {
			return count;
		}


		public int getPageSize() {
			return pageSize;
		}


		public int getPageRecord() {
			return pageRecord;
		}


		public String getPage() {
			return page;
		}
		
	
	
}
