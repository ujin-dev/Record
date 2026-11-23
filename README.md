<div align="center">

# Re:cord

**영화와 TV 시리즈, 책의 감상을 달력에 기록하는 Android 앱**

기록을 남기고 싶은 날에 평점, 감상, 태그와 사진을 함께 저장해 보세요.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Minimum SDK](https://img.shields.io/badge/minSdk-26-brightgreen?logo=android&logoColor=white)

</div>

## 소개

Re:cord는 영화, TV 시리즈, 책을 감상한 기록을 한곳에 모아 관리하는 앱입니다. 달력에서 감상한 날을 돌아보고, 각 리뷰의 평점과 메모, 사진을 확인할 수 있습니다.

## 주요 기능

| 기능 | 설명 |
| --- | --- |
| 월간 달력 | 날짜별 리뷰와 월간 기록을 한눈에 확인 |
| 작품 검색 | TMDB에서 영화와 TV 시리즈를 검색해 리뷰에 연결 |
| 리뷰 작성 | 평점, 감상 내용, 태그를 기록하고 사진 최대 3장 첨부 |
| 리뷰 관리 | 리뷰 상세 확인, 수정 및 삭제 |
| 로컬 저장 | Room에 리뷰와 이미지 정보를 저장하고 첨부 이미지는 앱 내부 저장소에서 관리 |

## 기술 스택

- **Language / UI:** Kotlin, Jetpack Compose, Material 3
- **아키텍처:** 현재 Clean Architecture와 Google 앱 아키텍처가 혼재되어 있으며, 최종 목표는 Clean Architecture입니다.
- **Async:** Coroutines, Flow
- **Local storage:** Room
- **Network:** Retrofit, TMDB API
- **Dependency injection:** Hilt
- **Image loading:** Coil
- **Navigation:** Navigation Compose

## 시작하기

### 요구 사항

- Android Studio 및 Android SDK 37
- JDK 11 이상
- Android 8.0 (API 26) 이상 기기 또는 에뮬레이터
- TMDB API Read Access Token

### 설정 및 실행

1. 저장소를 clone한 뒤 Android Studio에서 프로젝트를 엽니다.
2. 프로젝트 루트의 `local.properties`에 TMDB 토큰을 추가합니다. 기존 `sdk.dir` 설정은 유지하세요.

   ```properties
   TMDB_ACCESS_TOKEN=발급받은_TMDB_API_Read_Access_Token
   ```

   토큰은 [TMDB 설정 페이지](https://www.themoviedb.org/settings/api)에서 발급할 수 있습니다. 토큰은 공개하지 마세요.

3. Gradle 동기화가 끝나면 `app` 구성을 실행합니다.

명령줄에서 debug 빌드를 만들 수도 있습니다.

```shell
./gradlew assembleDebug
```

Windows에서는 다음 명령을 사용합니다.

```powershell
.\gradlew.bat assembleDebug
```

## 프로젝트 구조

```text
kotlin/com/yujin/record/
├── data/                 # 데이터 계층
│   ├── database/         # Room DB, DAO, Entity, 매퍼
│   ├── remote/           # TMDB API, DTO, 모델, 매퍼
│   ├── repository/       # Repository 구현
│   └── storage/          # 이미지 파일 저장
├── di/                   # Hilt 의존성 모듈
├── domain/               # 도메인 계층
│   ├── model/            # 앱 모델
│   ├── repository/       # Repository 인터페이스
│   └── usecase/          # 앱 유스케이스
├── navigation/           # Compose 내비게이션
├── presentation/         # 화면 및 UI 상태 관리
│   ├── calendar/         # 달력 화면과 컴포넌트
│   ├── reviewdetail/     # 리뷰 상세 화면
│   └── reviewform/       # 리뷰 작성·수정 화면
└── ui/                   # 공통 미리보기와 테마
```

## 데이터 및 출처

리뷰와 이미지 메타데이터는 Room에 저장합니다. 첨부 이미지는 앱 내부 저장소에 복사해 관리합니다. 영화와 TV 시리즈 검색 및 상세 정보는 TMDB API를 사용하며, 책 리뷰의 외부 검색 연동은 제공하지 않습니다.

TMDB의 데이터와 이미지는 TMDB 서비스 약관 및 사용 정책을 따릅니다.
