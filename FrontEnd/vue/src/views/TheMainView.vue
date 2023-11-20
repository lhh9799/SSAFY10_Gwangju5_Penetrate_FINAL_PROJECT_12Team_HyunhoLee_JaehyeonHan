<script setup></script>

<template>
  <div id='wrapper'>
			<!-- 메인 페이지 상단 시작 -->
			<div class="row container-xl" style="float: none; margin: auto;">
				<!-- 캐러셀 시작 -->
				<div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
					<div class="carousel-indicators">
						<button type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
						<button type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide-to="1" aria-label="Slide 2"></button>
						<button type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide-to="2" aria-label="Slide 3"></button>
					</div>
					
					<div class="carousel-inner">
						<div class="carousel-item active">
              <img src="@/assets/images/carousel_01.png" class="d-block w-100" alt="...">
						</div>
						<div class="carousel-item">
							<img src="@/assets/images/carousel_02.png" class="d-block w-100" alt="...">
						</div>
						<div class="carousel-item">
							<img src="@/assets/images/carousel_03.png" class="d-block w-100" alt="...">
						</div>
					</div>
					<button class="carousel-control-prev" type="button"
						data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
						<span class="carousel-control-prev-icon" aria-hidden="true"></span>
						<span class="visually-hidden">Previous</span>
					</button>
					<button class="carousel-control-next" type="button"
						data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
						<span class="carousel-control-next-icon" aria-hidden="true"></span>
						<span class="visually-hidden">Next</span>
					</button>
				</div>
			</div>
			<!-- 캐러셀 끝 -->
	
			<p></p>
	
			<div>
				<!-- 카드 그룹 시작 -->
				<div class="row container-xl" style="float: none; margin: auto;">
					<h2>여행 계획</h2>
					<div class="card col-md-4 p-2">
						<img class="card-img-top" src="@/assets/images/card_01.jpg">
						<div class="card-body">
							<h5 class="card-title">반려견과 떠나는 여행</h5>
							<p class="card-text">안전한 여행을 위해 켄넬을 준비해주세요.</p>
						</div>
						<div class="card-footer">
							<small class="text-muted">2023/09/09</small>
						</div>
					</div>
					<div class="card col-md-4 p-2">
						<img class="card-img-top" src="@/assets/images/card_02.jpg">
						<div class="card-body">
							<h5 class="card-title">관광지 중심으로</h5>
							<p class="card-text">강아지가 출입 가능한 관광지를 모았습니다.</p>
						</div>
						<div class="card-footer">
							<small class="text-muted">2023/09/05</small>
						</div>
					</div>
					<div class="card col-md-4 p-2">
						<img class="card-img-top" src="@/assets/images/card_03.jpg">
						<div class="card-body">
							<h5 class="card-title">어트렉션</h5>
							<p class="card-text">주의사항을 확인해주세요.</p>
						</div>
						<div class="card-footer">
							<small class="text-muted">2023/08/30</small>
						</div>
					</div>
					<div class="card col-md-4 p-2">
						<img class="card-img-top" src="@/assets/images/card_04.jpg">
						<div class="card-body">
							<h5 class="card-title">숙소 중심으로</h5>
							<p class="card-text">야간에도 활동할 수 있게 숙소에 가까운 곳으로 골랐습니다.</p>
						</div>
						<div class="card-footer">
							<small class="text-muted">2023/08/12</small>
						</div>
					</div>
					<div class="card col-md-4 p-2">
						<img class="card-img-top" src="@/assets/images/card_05.jpg">
						<div class="card-body">
							<h5 class="card-title">휴양</h5>
							<p class="card-text">넓고 인기가 많은 장소를 안내해드립니다.</p>
						</div>
						<div class="card-footer">
							<small class="text-muted">2023/08/02</small>
						</div>
					</div>
					<div class="card col-md-4 p-2">
						<img class="card-img-top" src="@/assets/images/card_06.jpg">
						<div class="card-body">
							<h5 class="card-title">반려견 사진관</h5>
							<p class="card-text">여행하며 찍은 사진을 자랑해주세요.</p>
						</div>
						<div class="card-footer">
							<small class="text-muted">2023/07/22</small>
						</div>
					</div>
				</div>
				<!-- 카드 그룹 끝 -->
			</div>
  </div>
</template>

<style scoped></style>
