<script>
import { ref } from "vue";
export default {
  data() {
    return {
      options: ref(["꽝"]),
      selectedTravel: "",
      startAngle: 0,
      spinTimeout: null,
      spinArcStart: 10,
      spinTime: 0,
      spinTimeTotal: 0,
      ctx: null,
    };
  },
  methods: {
    add(id, event) {
      if (id) {
        this.options.push(id);
        console.log(this.options);
        this.drawRouletteWheel();
      }
    },
    byte2Hex(n) {
      var nybHexString = "0123456789ABCDEF";
      return String(nybHexString.substr((n >> 4) & 0x0f, 1)) + nybHexString.substr(n & 0x0f, 1);
    },
    RGB2Color(r, g, b) {
      return "#" + this.byte2Hex(r) + this.byte2Hex(g) + this.byte2Hex(b);
    },
    getColor(item, maxitem) {
      var phase = 0;
      var center = 128;
      var width = 127;
      var frequency = (Math.PI * 2) / maxitem;

      var red = Math.sin(frequency * item + 2 + phase) * width + center;
      var green = Math.sin(frequency * item + 0 + phase) * width + center;
      var blue = Math.sin(frequency * item + 4 + phase) * width + center;

      return this.RGB2Color(red, green, blue);
    },
    drawRouletteWheel() {
      var canvas = this.$refs.canvas;

      if (canvas.getContext) {
        var outsideRadius = 200;
        var textRadius = 160;
        var insideRadius = 125;

        this.ctx = canvas.getContext("2d");
        this.ctx.clearRect(0, 0, 500, 500);

        this.ctx.strokeStyle = "black";
        this.ctx.lineWidth = 2;

        this.ctx.font = "bold 12px Helvetica, Arial";

        for (var i = 0; i < this.options.length; i++) {
          var angle = this.startAngle + (i * Math.PI) / (this.options.length / 2);
          this.ctx.fillStyle = this.getColor(i, this.options.length);

          this.ctx.beginPath();
          this.ctx.arc(
            250,
            250,
            outsideRadius,
            angle,
            angle + Math.PI / (this.options.length / 2),
            false
          );
          this.ctx.arc(
            250,
            250,
            insideRadius,
            angle + Math.PI / (this.options.length / 2),
            angle,
            true
          );
          this.ctx.stroke();
          this.ctx.fill();

          this.ctx.save();
          this.ctx.shadowOffsetX = -1;
          this.ctx.shadowOffsetY = -1;
          this.ctx.shadowBlur = 0;
          this.ctx.shadowColor = "rgb(220,220,220)";
          this.ctx.fillStyle = "black";
          this.ctx.translate(
            250 + Math.cos(angle + Math.PI / (this.options.length / 2) / 2) * textRadius,
            250 + Math.sin(angle + Math.PI / (this.options.length / 2) / 2) * textRadius
          );
          this.ctx.rotate(angle + Math.PI / (this.options.length / 2) / 2 + Math.PI / 2);
          var text = this.options[i];
          this.ctx.fillText(text, -this.ctx.measureText(text).width / 2, 0);
          this.ctx.restore();
        }

        // Arrow
        this.ctx.fillStyle = "black";
        this.ctx.beginPath();
        this.ctx.moveTo(250 - 4, 250 - (outsideRadius + 5));
        this.ctx.lineTo(250 + 4, 250 - (outsideRadius + 5));
        this.ctx.lineTo(250 + 4, 250 - (outsideRadius - 5));
        this.ctx.lineTo(250 + 9, 250 - (outsideRadius - 5));
        this.ctx.lineTo(250 + 0, 250 - (outsideRadius - 13));
        this.ctx.lineTo(250 - 9, 250 - (outsideRadius - 5));
        this.ctx.lineTo(250 - 4, 250 - (outsideRadius - 5));
        this.ctx.lineTo(250 - 4, 250 - (outsideRadius + 5));
        this.ctx.fill();
      }
    },
    spin() {
      this.spinAngleStart = Math.random() * 10 + 10;
      this.spinTime = 0;
      this.spinTimeTotal = Math.random() * 3 + 4 * 1000;
      this.rotateWheel();
    },
    rotateWheel() {
      this.spinTime += 30;
      if (this.spinTime >= this.spinTimeTotal) {
        this.stopRotateWheel();
        return;
      }
      var spinAngle =
        this.spinAngleStart -
        this.easeOut(this.spinTime, 0, this.spinAngleStart, this.spinTimeTotal);
      this.startAngle += (spinAngle * Math.PI) / 180;
      this.drawRouletteWheel();
      this.spinTimeout = setTimeout(() => this.rotateWheel(), 30);
    },
    stopRotateWheel() {
      clearTimeout(this.spinTimeout);
      var degrees = (this.startAngle * 180) / Math.PI + 90;
      var arcd = ((Math.PI / (this.options.length / 2)) * 180) / Math.PI;
      var index = Math.floor((360 - (degrees % 360)) / arcd);
      this.ctx.save();
      this.ctx.font = "bold 30px Helvetica, Arial";
      var text = this.options[index];
      localStorage.setItem("result", text);
      this.ctx.fillText(text, 250 - this.ctx.measureText(text).width / 2, 250 + 10);
      this.ctx.restore();

      // Add a setTimeout to delay the execution of the search function
      setTimeout(() => {}, 100);
    },
    easeOut(t, b, c, d) {
      var ts = (t /= d) * t;
      var tc = ts * t;
      return b + c * (tc + -3 * ts + 3 * t);
    },
  },
  mounted() {
    this.drawRouletteWheel();
  },
};
</script>
<script setup>
import { ref } from "vue";

const travelList = ref([
  "서울",
  "인천",
  "대전",
  "대구",
  "광주",
  "부산",
  "울산",
  "세종특별자치시",
  "경기도",
  "강원도",
  "충청북도",
  "충청남도",
  "경상북도",
  "경상남도",
  "전라북도",
  "전라남도",
  "제주도",
]);

// 유튜브 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
const inputText = ref("");
const videos = ref([]);
const youtubeAPI = "AIzaSyC0jFEm56-TWkfH9i4qy1cH76qytsX2Dvc";

const search = () => {
  setTimeout(() => {
    inputText.value = localStorage.getItem("result");

    if (inputText.value === "꽝") {
      alert("꽝입니다! 다시 돌려보세요!");
      return;
    }
    const youtubeURL = `https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=3&q=${inputText.value} vlog&type=video&key=${youtubeAPI}`;

    fetch(youtubeURL)
      .then((response) => response.json())
      .then((data) => {
        videos.value = data.items;
      })
      .catch((error) => console.error("Error fetching YouTube API:", error));

    if (inputText.value === "서울") {
      lat.value = 37.583328;
      lon.value = 126.977969;
    } else if (inputText.value === "강원도") {
      lat.value = 37.874722;
      lon.value = 127.734169;
    } else if (inputText.value === "광주") {
      lat.value = 35.166672;
      lon.value = 126.916672;
    } else if (inputText.value === "경기도") {
      lat.value = 37.741501;
      lon.value = 127.047401;
    } else if (inputText.value === "경상남도") {
      lat.value = 35.228062;
      lon.value = 128.681107;
    } else if (inputText.value === "경상북도") {
      lat.value = 36.565559;
      lon.value = 128.725006;
    } else if (inputText.value === "부산") {
      lat.value = 35.133331;
      lon.value = 129.050003;
    } else if (inputText.value === "울산") {
      lat.value = 35.566669;
      lon.value = 129.266663;
    } else if (inputText.value === "인천") {
      lat.value = 37.450001;
      lon.value = 126.416107;
    } else if (inputText.value === "전라남도") {
      lat.value = 34.989719;
      lon.value = 126.47139;
    } else if (inputText.value === "전라북도") {
      lat.value = 35.821941;
      lon.value = 127.148888;
    } else if (inputText.value === "제주도") {
      lat.value = 33.50972;
      lon.value = 126.521942;
    } else if (inputText.value === "충청남도") {
      lat.value = 36.5184;
      lon.value = 126.8;
    } else if (inputText.value === "충청북도") {
      lat.value = 36.637218;
      lon.value = 127.489723;
    } else if (inputText.value === "세종특별자치시") {
      lat.value = 36.4800121;
      lon.value = 127.2890691;
    } else if (inputText.value === "대전") {
      lat.value = 36.35044;
      lon.value = 127.3845;
    } else if (inputText.value === "대구") {
      lat.value = 35.8714;
      lon.value = 128.6014;
    }

    regionName.value = inputText.value;

    let weatherURL = `${url_base}weather?lat=${lat.value}&lon=${lon.value}&APPID=${weaterAPI}`;
    fetch(weatherURL)
      .then((res) => res.json())
      .then((results) => setResult(results));
  }, 4500);
};
// 날씨 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

const weaterAPI = "71178a1660c842af87a8bdb5b75ad159";
const url_base = "https://api.openweathermap.org/data/2.5/";

const lat = ref({});
const lon = ref({});
const regionName = ref({});

const weather = ref({});

const setResult = (results) => {
  weather.value = results;
};

const dateBuilder = () => {
  let d = new Date();
  let months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];
  let days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  let day = days[d.getDay()];
  let date = d.getDate();
  let month = months[d.getMonth()];
  let year = d.getFullYear();
  return `${day} ${date} ${month} ${year}`;
};

// 룰렛
const selectedTravel = ref();
</script>

<template>
  <div class="parent">
    <!-- 날씨 -->
    <div class="weather">
      <div
        id="app"
        :class="
          typeof weather.main !== 'undefined' && Math.round(weather.main.temp) > 16 ? 'warm' : ''
        "
      >
        <main>
          <div class="weather-wrap" v-if="typeof weather.main !== 'undefined'">
            <div class="location-box">
              <div class="location">{{ regionName }}, {{ weather.sys.country }}</div>
              <div class="date">{{ dateBuilder() }}</div>
            </div>
            <div class="weather-box">
              <div class="temp">{{ Math.round(weather.main.temp) - 273 }}℃</div>
            </div>
          </div>
        </main>
      </div>
    </div>

    <!-- 룰렛 -->

    <div class="roulette">
      <div class="roulette-container">
        <div>
          <canvas ref="canvas" width="500" height="500"></canvas>
        </div>
        <div id="addDiv">
          <button class="button-with-space" @click="[spin(), search()]">SPIN</button>
          <div>
            <select v-model="selectedTravel" class="input-with-space">
              <option v-for="location in travelList" :key="location" :value="location">
                {{ location }}
              </option>
            </select>
            <button class="button-with-space" @click="add(selectedTravel, $event)">
              여행지 추가
            </button>
          </div>
        </div>

        <!-- <select v-model="tripDelete" class="input-with-space">
          <option v-for="location in product" :key="location" :value="location">
            {{ location }}
          </option>
        </select>
        <button class="button-with-space" @click="drop">여행지 삭제</button> -->
      </div>
    </div>

    <!-- 유튜브 -->
    <div class="youtube">
      <div class="youtubeThumb">
        <div class="youtubeTitle">관련 영상</div>
        <div v-for="video in videos" :key="video.id">
          <a :href="'https://www.youtube.com/watch?v=' + video.id.videoId" target="_blank">
            <img :src="video.snippet.thumbnails.medium.url" alt="Video Thumbnail" />
            <p class="font">{{ video.snippet.title }}</p>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
#addDiv {
  text-align: center;
}
.youtubeTitle {
  margin-bottom: 20px;
  text-align: center;
  font-weight: bold;
  font-size: 30px;
}
.font {
  color: black;
  margin-top: 10px;
  margin-bottom: 30px;
  font-size: 14px;
}
body {
  background: #f7f8fc;
  height: 960px;
}

canvas {
  margin-top: 100px;
  transition: 2s;
}

button {
  z-index: 1000;
  background: #b2bec3;
  color: white;
  margin-top: 1rem;
  padding: 0.8rem 1.8rem;
  border: none;
  font-size: 1.5rem;
  font-weight: bold;
  border-radius: 5px;
  transition: 0.2s;
  cursor: pointer;
}

select {
  width: 50%;
}

button:active {
  background: #444;
  color: #f9f9f9;
}

#menu::before {
  margin-top: 100px;
  content: "";
  position: absolute;
  width: 10px;
  height: 0;
}

.button-with-space {
  margin-right: 12px;
}

.input-with-space {
  margin-right: 12px;
}

.parent {
  width: 90%;
  margin: 10px auto;
  width: 90%;
  margin: 10px auto;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.weather {
  flex: 1;
  width: 30%;
  box-sizing: border-box;
}

.roulette {
  flex: 1;
  margin: 0px 5%;
  width: 30%;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.youtube {
  flex: 1;
  width: 30%;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  text-align: center;
  align-self: flex-start;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: "montserrat", sans-serif;
}
#app {
  background-size: cover;
  background-position: bottom;
  transition: 0.4s;
}
main {
  height: 550px;
  padding: 25px;
  border-radius: 20px;
  background-image: linear-gradient(to bottom, rgba(0, 0, 0, 0.25), rgba(0, 0, 0, 0.75));
}
.search-box {
  width: 100%;
  margin-bottom: 30px;
}
.search-box .search-bar {
  display: block;
  width: 100%;
  padding: 15px;

  color: #313131;
  font-size: 20px;
  appearance: none;
  border: none;
  outline: none;
  background: none;
  box-shadow: 0px 0px 8px rgba(0, 0, 0, 0.25);
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 0px 16px 0px 16px;
  transition: 0.4s;
}
.search-box .search-bar:focus {
  box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.25);
  background-color: rgba(255, 255, 255, 0.75);
  border-radius: 16px 0px 16px 0px;
}
.location-box .location {
  color: #fff;
  font-size: 32px;
  font-weight: 500;
  text-align: center;
  text-shadow: 1px 3px rgba(0, 0, 0, 0.25);
}
.location-box .date {
  color: #fff;
  font-size: 20px;
  font-weight: 300;
  font-style: italic;
  text-align: center;
}
.weather-box {
  text-align: center;
}
.weather-box .temp {
  display: inline-block;
  padding: 10px 25px;
  color: #fff;
  font-size: 102px;
  font-weight: 900;
  text-shadow: 3px 6px rgba(0, 0, 0, 0.25);
  background-color: rgba(255, 255, 255, 0.25);
  border-radius: 16px;
  margin: 30px 0px;
  box-shadow: 3px 6px rgba(0, 0, 0, 0.25);
}
.weather-box .weather {
  color: #fff;
  font-size: 48px;
  font-weight: 700;
  font-style: italic;
  text-shadow: 3px 6px rgba(0, 0, 0, 0.25);
}
</style>
