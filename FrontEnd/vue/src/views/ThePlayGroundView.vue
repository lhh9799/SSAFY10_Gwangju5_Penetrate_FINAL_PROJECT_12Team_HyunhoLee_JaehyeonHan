<script>
export default {
  data() {
    return {
      options: ["1", "2", "3", "4", "5", "6", "7", "8"],
      startAngle: 0,
      // arc: Math.PI / (this.options.length / 2),
      arc: Math.PI / 1.5,
      spinTimeout: null,
      spinArcStart: 10,
      spinTime: 0,
      spinTimeTotal: 0,
      ctx: null,
    };
  },
  methods: {
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
          var angle = this.startAngle + i * this.arc;
          this.ctx.fillStyle = this.getColor(i, this.options.length);

          this.ctx.beginPath();
          this.ctx.arc(250, 250, outsideRadius, angle, angle + this.arc, false);
          this.ctx.arc(250, 250, insideRadius, angle + this.arc, angle, true);
          this.ctx.stroke();
          this.ctx.fill();

          this.ctx.save();
          this.ctx.shadowOffsetX = -1;
          this.ctx.shadowOffsetY = -1;
          this.ctx.shadowBlur = 0;
          this.ctx.shadowColor = "rgb(220,220,220)";
          this.ctx.fillStyle = "black";
          this.ctx.translate(
            250 + Math.cos(angle + this.arc / 2) * textRadius,
            250 + Math.sin(angle + this.arc / 2) * textRadius
          );
          this.ctx.rotate(angle + this.arc / 2 + Math.PI / 2);
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
      var arcd = (this.arc * 180) / Math.PI;
      var index = Math.floor((360 - (degrees % 360)) / arcd);
      this.ctx.save();
      this.ctx.font = "bold 30px Helvetica, Arial";
      var text = this.options[index];
      this.ctx.fillText(text, 250 - this.ctx.measureText(text).width / 2, 250 + 10);
      this.ctx.restore();
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
  const youtubeURL = `https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=2&q=${inputText.value}&type=video&key=${youtubeAPI}`;

  fetch(youtubeURL)
    .then((response) => response.json())
    .then((data) => {
      videos.value = data.items;
    })
    .catch((error) => console.error("Error fetching YouTube API:", error));
};

// 날씨 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

const api_key = "71178a1660c842af87a8bdb5b75ad159";
const url_base = "https://api.openweathermap.org/data/2.5/";

const query = ref("");
const weather = ref({});

const fetchWeather = (e) => {
  if (e.key === "Enter") {
    let fetchUrl = `${url_base}weather?q=${query.value}&units=metric&APPID=${api_key}`;
    fetch(fetchUrl)
      .then((res) => res.json())
      .then((results) => setResult(results));
  }
};

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
          <div class="search-box">
            <input
              type="text"
              class="search-bar"
              placeholder="Search..."
              v-model="query"
              @keypress="fetchWeather"
            />
          </div>
          <div class="weather-wrap" v-if="typeof weather.main !== 'undefined'">
            <div class="location-box">
              <div class="location">{{ weather.name }}, {{ weather.sys.country }}</div>
              <div class="date">{{ dateBuilder() }}</div>
            </div>
            <div class="weather-box">
              <div class="temp">{{ Math.round(weather.main.temp) }}℃</div>
            </div>
          </div>
        </main>
      </div>
    </div>

    <!-- 룰렛 -->
    <div>
      <input type="button" value="Spin" style="float: left" @click="spin" />
      <canvas ref="canvas" width="500" height="500"></canvas>
    </div>

    <!-- 유튜브 -->
    <div class="youtube">
      <form @submit.prevent="search">
        <input v-model="inputText" placeholder="검색어를 입력하세요" />
        <button type="submit">검색</button>
      </form>
      <ul>
        <li v-for="video in videos" :key="video.id">
          <a :href="'https://www.youtube.com/watch?v=' + video.id.videoId" target="_blank">
            <img :src="video.snippet.thumbnails.default.url" alt="Video Thumbnail" />
            <p>{{ video.snippet.title }}</p>
          </a>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
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

.roulette-pin {
  position: absolute;
  top: 9%;
  left: 50%;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 25px 15px 0 15px;
  border-color: black transparent transparent transparent;
  margin-left: -15px;
  z-index: 1;
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
}

.weather {
  border: 1px solid red;
  flex: 1;
  width: 30%;
  box-sizing: border-box;
}

.roulette {
  border: 1px solid green;
  flex: 1;
  margin: 0px 5%;
  width: 30%;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
}

.youtube {
  border: 1px solid blue;
  flex: 1;
  width: 30%;
  box-sizing: border-box;
  display: flex;
  justify-content: center;
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
  background-image: url("./assets/cold-bg.jpg");
  background-size: cover;
  background-position: bottom;
  transition: 0.4s;
}
#app.warm {
  background-image: url("./assets/warm-bg.jpg");
}
main {
  min-height: 100vh;
  padding: 25px;
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
