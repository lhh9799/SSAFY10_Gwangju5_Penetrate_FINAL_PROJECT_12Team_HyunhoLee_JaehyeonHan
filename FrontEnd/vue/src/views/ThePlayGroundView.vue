User
<script setup>
import { ref, onMounted } from "vue";

const myCanvas = ref(null);
const tripAdd = ref("");
const tripDelete = ref("");
const product = ref(["다시돌려!"]);
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

const rotate = () => {
  myCanvas.value.style.transform = "initial";
  myCanvas.value.style.transition = "initial";

  const rotateRoulette = () => {
    const alpha = Math.floor(Math.random() * 100);
    const ran = Math.floor(Math.random() * product.value.length);
    const arc = 360 / product.value.length;
    const rotate = ran * arc + 3600 + arc * 3 - arc / 4 + alpha;

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
  }, 100);
};

const add = () => {
  if (!(product.value.indexOf(tripAdd.value) == -1)) {
    alert("이미 룰렛에 있는 여행지입니다!");
  } else if (tripAdd.value !== undefined && tripAdd.value !== "") {
    product.value.push(tripAdd.value);
    colors.value.push(colors.value.shift());
    newMake();
    tripAdd.value = "";
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
</script>

<template>
  <div id="menu">
    <div class="roulette-pin"></div>
    <canvas ref="myCanvas" width="600" height="600"></canvas>
    <div>
      <button class="button-with-space" @click="rotate">START</button>
      <button class="button-with-space" @click="stopRotate">STOP</button>
    </div>
    <div id="addDiv">
      <input class="input-with-space" type="text" v-model="tripAdd" />
      <button class="button-with-space" @click="add">여행지 추가</button>
      <input class="input-with-space" type="text" v-model="tripDelete" />
      <button class="button-with-space" @click="drop">여행지 삭제</button>
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

button:active {
  background: #444;
  color: #f9f9f9;
}

#menu {
  width: 100%;
  height: 960px;
  overflow: hidden;
  display: flex;
  align-items: center;
  text-align: center;
  flex-direction: column;
  position: relative;
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
</style>
