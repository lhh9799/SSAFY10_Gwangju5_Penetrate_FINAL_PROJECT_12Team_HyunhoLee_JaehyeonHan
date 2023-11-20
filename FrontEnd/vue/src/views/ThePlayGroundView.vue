User
<script setup>
import { ref, onMounted } from "vue";

const myCanvas = ref(null);
const menuAdd = ref("");
const product = ref(["햄버거", "순대국", "정식당", "중국집", "구내식당"]);
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
  "#b2bec3",
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
  if (menuAdd.value !== undefined && menuAdd.value !== "") {
    product.value.push(menuAdd.value);
    colors.value.push(colors.value.shift()); // Move the first color to the end for a rotating effect
    newMake();
    menuAdd.value = "";
  } else {
    alert("메뉴를 입력한 후 버튼을 클릭 해 주세요");
  }
};

onMounted(newMake);
</script>

<template>
  <div id="menu">
    <div class="roulette-pin"></div>
    <canvas ref="myCanvas" width="600" height="600"></canvas>
    <button @click="rotate">START</button>
    <button @click="stopRotate">STOP</button>
    <div id="addDiv">
      <input type="text" v-model="menuAdd" />
      <button @click="add">메뉴 추가</button>
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

  width: 20px;
  height: 20px;
  border-style: solid;
  border-width: 25px 5px 0 5px;
  border-color: black transparent transparent transparent;

  margin-left: -5px;
}
</style>
