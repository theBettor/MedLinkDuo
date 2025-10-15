# 🚀 MedLinkDuo — Voice-first BLE Measurement

**목표:** “화면 없이도 쓸 수 있는” BLE 측정 앱.

**핵심:** **음성(TTS) + 진동/사운드 + 제스처**로 권한 → 연결 → 측정 → 피드백을 끊김 없이 완주하도록 설계했습니다.

## 다운로드

[⬇️ Android APK 받기](https://github.com/theBettor/MedLinkDuo/releases/tag/apk)

## 데모영상

---

## 🗺️ 아키텍처 한 눈에

```
com.bettor.medlinkduo
├─ core
│  ├─ common      # AudioFocusManager, PermissionMgr, PhaseAndUiState
│  ├─ di          # Hilt 모듈(AppModule, DispatcherModule)
│  └─ ui          # A11yHelpers, ActionGuard, Haptics, SensoryFeedback, Voice
├─ data
│  ├─ ble         # MockBleRepository, BleKeepAliveService
│  ├─ measure     # MeasurementSupervisor
│  └─ tts         # AndroidTtsController (speak / speakAndWait 안정화)
├─ ui
│  ├─ screens     # ScanConnectScreen, MeasurementScreen, FeedbackScreen
│  ├─ MainActivity
│  └─ PermissionActivity (런처; 권한 다이얼로그 드라이버)
└─ domain         # 인터페이스 계층(BLE, TTS 등)

```

**왜 이렇게?**

- 기능별로 분리해 **읽히는 코드**를 만들고, 교체 가능한 `data` / 안정적인 `core` 유틸로 **테스트·확장**을 쉽게 했습니다.

---

## 📱 화면 4종 요약 (제스처 정책 포함)

> 전 화면 공통: 더블탭 = 음성 명령 시작
> 
> 
> 화면별 롱프레스: **Scan=재탐색 · Measurement=긴급 중단 · Feedback=닫고 스캔 복귀**
> 

### 1) PermissionActivity — *런처, 권한 UX*

- 앱 진입 시 **자동 1회** 시스템 권한 다이얼로그.
- 거부 시 **단일 탭으로 재요청**(TalkBack 더블탭도 동일 onClick 경로).
- **‘허용 안함’ 반복(영구거부)** 감지 → 재요청 비활성 + TTS로 재실행 안내.
- 설정 화면으로 보내지 않고 **앱 내에서 해결**하는 전략.

**핵심 객체:** `PermissionMgr`, `AndroidTtsController`, `SensoryFeedback`, `A11yHelpers.a11yGestures`.

---

### 2) ScanConnectScreen — *기기 스캔/선택*

- 진입·RESUMED 시 `vm.ensureScan()`으로 **자동 스캔 보장**.
- **롱프레스 = 재탐색**(tick + ScanStart 햅틱).
- 장치 항목은 `a11yClickable(desc, label)`로 **상태 설명 & 행동 라벨** 제공.
- 리스트 안정화: `items(..., key = { it.id })` + 최대 2개 노출(빠른 선택 흐름).

**핵심 객체:** `ConnectViewModel`, `VoiceButton`, `Haptics`, `SensoryFeedback`.

---

### 3) MeasurementScreen — *측정/중단/종료*

- 버튼은 `ActionGuard`로 **중복 클릭 방지**.
- **롱프레스 = 긴급 중단**(SafeStop 햅틱 + 에러 사운드).
- `ON_PAUSE`에서 `vm.pause()` 호출로 **유령 측정/발화 방지**.

**핵심 객체:** `SessionViewModel`, `rememberActionGuard`, `Haptics`, `TTS`.

---

### 4) FeedbackScreen — *마지막 결과 요약*

- 결과 카드로 **최종 값**만 또렷하게 전달.
- **롱프레스 = 닫고 스캔 복귀**.
- Nav 백스택 parent entry로 **Measurement와 동일 VM 공유**(상태 일관).

**핵심 객체:** `SessionViewModel`, `Haptics`, `TTS`.

---

## 🧏 접근성 & 감각 피드백 플레이북

- **제스처 헬퍼(A11yHelpers)**: 더블탭/롱프레스를 어디든 쉽게 부착, TalkBack 더블탭도 onClick으로 자연스럽게 연결.
- **a11yClickable(desc, label)**: 리스트 항목에 “상태 설명 + 행동 라벨”을 부여해 **탐색 품질** 향상.
- **SensoryFeedback + Haptics**: tick/success/error + 플랫폼 햅틱 이벤트로 **눈을 쓰지 않아도** 진행 상황을 인지.

---

## 🔊 TTS 품질 안정화(한 곳에서 해결)

`AndroidTtsController`가 다음을 보장합니다.

- **AudioFocus** 획득/반납을 발화 시작/종료/에러/stop에 맞춰 **ref-count**로 안전 처리.
- `speakAndWait()`가 **stop/cancel도 정상 흐름**으로 돌려주어 UI 코루틴 취소 안전.
- 앱 종료 시 **잔여 포커스 정리**.
    
    → 겹치는 발화, 중간 중단, 포커스 누수 같은 TTS 난제를 중앙에서 해결합니다.
    

---

## 🌐 문자열 리소스 & 현지화

모든 노출 텍스트는 `strings.xml`로 이동했습니다.

```xml
<string name="scan_title">기기 선택</string>
<string name="scan_device_desc">장치 %1$s, 신호 %2$d</string>
<string name="measurement_title">측정</string>
<string name="measurement_waiting">측정 대기 중…</string>
<string name="feedback_last_value">마지막 측정값: %1$s</string>

```

**효과:** 카피 수정/현지화 속도 ↑, 하드코딩 회귀 ↓.

---

## ✅ 코드 퀄리티 게이트

- **ktlint / ktlintFormat** ✨
    
    포맷을 표준화해 리뷰 비용 절감(예: 단일 if then 블록 금지).
    
- **Inspect Code** 🔍
    
    *UnusedDeclaration/Resources* 제거, 가시성 축소, 네이밍 교정 → **가벼운 바이너리**와 **읽히는 코드**.
    
- **단일 책임 분리** 🧱
    
    Permission은 **Activity에서 전담**, 측정/스캔 로직은 **ViewModel/UseCase**로 분리 → 유지보수 난이도↓.
    

---

## 💼 비즈니스 임팩트(요약)

- **학습 비용↓**: “더블탭=음성, 롱프레스=보조” 통일로 사용자 온보딩 시간 단축.
- **접근성↑**: 화면을 보지 않아도 **권한부터 결과까지** 완주 가능.
- **유지보수↓**: 중앙화된 TTS/피드백/권한 흐름 + `strings.xml` + 린팅으로 변경 비용 절감.
- **확장성↑**: Mock BLE → 실기기 교체가 쉬운 레이어드 구조.

---
