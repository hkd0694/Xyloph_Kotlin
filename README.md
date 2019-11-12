# Kotlin Study (8/9) - 2019/11/11

Kotlin을 공부하기 위해 간단한 앱부터 복잡한 앱까지 만들어 봄으로써 Kotlin에 대해 하기!

총 9개의 앱 중 여덟 번째 앱

프로젝트명 : Xylophone

기능

* 음판을 누르면 소리가 재생된다.


핵심 구성 요소

* SoundPool : 음원을 관리하고 재생하는 클래스이다.


라이브러리 설정

* 없음

## 소리 재생하기

1. 안드로이드에서 소리를 재생하는 방법
2. SoundPool 초기화 버전 분기
3. 음판에 동적으로 클릭 이벤트 정의하기


### 안드로이드에서 소리를 재생하는 방법

안드로이드에서 소리를 재생하는 방법은 대표적으로 MediaPlayer클래스와 SoundPool을 사용한다. 

일반적으로 소리 파일 연주에는 MediaPlayer 클래스를 사용한다. 이 클래스는 음악 파일과 비디오 파일을 모두 재생할 수 있다. 

```kotlin

val mediaPlayer = MediaPlayer.create(this,R.raw.do1)
button.setOnClickListener{ mediaPlayer.start() }
...

mediaPlayer.release()

```

MediaPlayer 클래스는 일반적으로 소리를 한 번만 재생하는 경우 또는 노래나 배경음악과 같은 경우에는 유용하다. 하지만 위 프로젝트처럼 실로폰을 같이 연타로 해서 연속으로 소리를 처리해야 하는 경우는 SoundPool 클래스가 더 유용하다.

```kotlin

SoundPool.Builder().setMaxStreams(8).build()

 val soundId = soundPool.load(this, pitch.second,1)
        findViewById<TextView>(pitch.first).setOnClickListener {
            soundPool.play(soundId, 1.0f,1.0f,0,0,1.0f)
        }

```

load() 메서드와 play() 메서드의 원형은 다음과 같다.

```kotlin
load(context: Context, resId: Int, priority: Int) :
```

* context : 컨텍스트를 지정한다. 액티비티를 지정한다.
* resId : 재생할 raw 디렉터리의 소리 파일 리소스를 지정한다.
* priority : 우선순위를 지정한다. 숫자가 높으면 우선순위가 높다.

```kotlin
play(soundId: Int, leftVolume: Float, rightVolume: Float, priority: Int, loop: Int, rate: Float)
```

* soundId : load() 메서드에서 반환된 음원의 id를 지정한다.
* leftVolume : 왼쪽 볼륨을 0.0 ~ 1.0 사이에서 지정한다.
* rightVolume : 오른쪽 볼륨을 0.0 ~ 1.0 사이에서 지정한다.
* priority : 우선순위를 지정한다. 0이 가장 낮은 순위이다.
* loop : 반복을 지정한다. 0이면 반복하지 않고 -1 이면 반복한다.
* rate : 재생 속도를 지정한다. 1.0이면 보통, 0.5dlaus 0.5배속이다.
  

### SoundPool 초기화 버전 분기

안드로이드의 SoundPool 클래스의 경우 5.0부터 방법이 변경되었다. 안드로이드 개발을 하다보면 API가 버전업되면서 생성자나 메서드에서 이런 경우를 자주 볼 수 있다.

그렇다면 어떻게 구분하면 될까? if문을 사용하여 롤리팝 이후와 이전에 다른 코드를 넣어주면 된다.

```kotlin

private val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder().setMaxStreams(8).build()
    } else {
        SoundPool(8,AudioManager.STREAM_MUSIC,0)
    }

```

### 음판에 동적으로 클릭 이벤트 정의하기

```kotlin

 private val sounds = listOf(
        Pair(R.id.do1,R.raw.do1),
        Pair(R.id.re,R.raw.re),
        Pair(R.id.mi,R.raw.mi),
        Pair(R.id.fa,R.raw.fa),
        Pair(R.id.sol,R.raw.sol),
        Pair(R.id.ra,R.raw.la),
        Pair(R.id.si,R.raw.si),
        Pair(R.id.do2,R.raw.do2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        //화면을 가로 모드로 고정시키기.. (Manifest에서도 설정 가능)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sounds.forEach{ tune(it)}
    }

    private fun tune(pitch: Pair<Int,Int>){
        val soundId = soundPool.load(this, pitch.second,1)
        findViewById<TextView>(pitch.first).setOnClickListener {
            soundPool.play(soundId, 1.0f,1.0f,0,0,1.0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

```
