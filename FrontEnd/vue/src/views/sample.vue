User
<script setup>
// 룰렛 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
import { ref, onMounted } from "vue";

const myCanvas = ref(null);
const selectedTravel = ref("");
const tripDelete = ref("");
const product = ref([]);
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
const colors = ref([
  "#55efc4",
  "#81ecec",
  "#74b9ff",
  "#a29bfe",
  "#dfe6e9",
  "#00b894",
  "#00cec9",
  "#0984e3",
  "#6c5ce7",
  "#ffeaa7",
  "#fab1a0",
  "#ff7675",
  "#fd79a8",
  "#636e72",
  "#fdcb6e",
  "#e17055",
  "#d63031",
  "#e84393",
  "#2d3436",
]);

const newMake = () => {
  const [cw, ch] = [myCanvas.value.width / 2, myCanvas.value.height / 2];
  const arc = Math.PI / (product.value.length / 2);
  const ctx = myCanvas.value.getContext("2d");

  for (let i = 0; i < product.value.length; i++) {
    ctx.beginPath();
    ctx.fillStyle = colors.value[i % colors.value.length];
    ctx.moveTo(cw, ch);
    ctx.arc(cw, ch, cw, arc * (i - 1), arc * i);
    ctx.fill();
    ctx.closePath();
  }

  ctx.fillStyle = "#fff";
  ctx.font = "18px Pretendard";
  ctx.textAlign = "center";

  for (let i = 0; i < product.value.length; i++) {
    const angle = arc * i + arc / 2;

    ctx.save();

    ctx.translate(cw + Math.cos(angle) * (cw - 50), ch + Math.sin(angle) * (ch - 50));

    ctx.rotate(angle + Math.PI / 2);

    product.value[i].split(" ").forEach((text, j) => {
      ctx.fillText(text, 0, 30 * j);
    });

    ctx.restore();
  }
};
let currentRotation = 0;
const rotate = () => {
  myCanvas.value.style.transform = "initial";
  myCanvas.value.style.transition = "initial";

  const rotateRoulette = () => {
    const alpha = Math.floor(Math.random() * 100);
    const ran = Math.floor(Math.random() * product.value.length);
    const arc = 360 / product.value.length;
    const rotate = ran * arc + 3600 + arc * 3 - arc / 4 + alpha;

    currentRotation = rotate; // Update the current rotation angle

    myCanvas.value.style.transform = `rotate(-${rotate}deg)`;
    myCanvas.value.style.transition = "0s";

    if (!stopFlag) {
      requestAnimationFrame(rotateRoulette);
    }
  };
  rotateRoulette();
};
let stopFlag = false;
const stopRotate = () => {
  stopFlag = true;

  setTimeout(() => {
    stopFlag = false;

    // Calculate the selected travel destination based on the current rotation
    const arc = 360 / product.value.length;
    const selectedTravelIndex =
      Math.floor((360 - (currentRotation % 360)) / arc) % product.value.length;
    const selectedTravelDestination = product.value[selectedTravelIndex];

    // Display an alert with the selected travel destination
    alert(`당첨 여행지: ${selectedTravelDestination}`);
  }, 100);
};
const add = () => {
  if (!(product.value.indexOf(selectedTravel.value) == -1)) {
    alert("이미 룰렛에 있는 여행지입니다!");
  } else if (selectedTravel.value !== undefined && selectedTravel.value !== "") {
    product.value.push(selectedTravel.value);
    colors.value.push(colors.value.shift());
    newMake();
    selectedTravel.value = "";
  } else {
    alert("여행지를 입력한 후 버튼을 클릭 해 주세요");
  }
};

const drop = () => {
  if (tripDelete.value !== undefined && tripDelete.value !== "") {
    console.log(tripDelete.value);
    const indexToRemove = product.value.indexOf(tripDelete.value);
    if (indexToRemove !== -1) {
      product.value.splice(indexToRemove, 1);
      newMake();
      tripDelete.value = "";
    } else {
      alert("룰렛에 없는 여행지입니다!");
    }
  } else {
    alert("여행지를 입력한 후 버튼을 클릭 해 주세요");
  }
};

onMounted(newMake);

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
    <div class="roulette">
      <div class="roulette-container">
        <div class="roulette-pin"></div>
        <canvas ref="myCanvas" width="500" height="500"></canvas>
        <div>
          <button class="button-with-space" @click="rotate">START</button>
          <button class="button-with-space" @click="stopRotate">STOP</button>
        </div>
        <div id="addDiv">
          <select v-model="selectedTravel" class="input-with-space">
            <option v-for="location in travelList" :key="location" :value="location">
              {{ location }}
            </option>
          </select>
          <button class="button-with-space" @click="add">여행지 추가</button>
        </div>

        <select v-model="tripDelete" class="input-with-space">
          <option v-for="location in product" :key="location" :value="location">
            {{ location }}
          </option>
        </select>
        <button class="button-with-space" @click="drop">여행지 삭제</button>
      </div>
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
