<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
<div class="min-h-screen bg-white">
<!-- Travel Plan Detail Page -->
<div class="w-full flex flex-col p-8">
<!-- Back Button -->
<div class="w-4/5 mx-auto mb-4">
<button
@click="goBackToList"
class="bg-[#9FB3DF] text-white px-4 py-2 rounded-button flex items-center gap-2 cursor-pointer whitespace-nowrap"
>
<i class="fas fa-arrow-left"></i> 목록으로 돌아가기
</button>
</div>
<!-- Plan Header -->
<div class="bg-[#FFF1D5] rounded-lg p-6 mb-6 shadow-md w-4/5 mx-auto">
<div class="flex justify-between items-start">
<div>
<h1 class="text-3xl font-bold mb-2">{{ selectedPlan.title }}</h1>
<div class="flex items-center gap-3 mb-4">
<span class="bg-[#9FB3DF] text-white px-3 py-1 rounded-md">{{ selectedPlan.category }}</span>
<span class="text-gray-600">작성자: {{ selectedPlan.author }}</span>
<span class="text-gray-600">지역: {{ selectedPlan.region }}</span>
</div>
</div>
<div @click="toggleFavorite(selectedPlan.id)" class="cursor-pointer">
<i
class="fas fa-star text-2xl"
:class="selectedPlan.favorite ? 'text-yellow-400' : 'text-gray-300'"
></i>
</div>
</div>
<!-- Route Overview -->
<div class="flex items-center gap-3 flex-wrap mt-2">
<template v-for="(location, locIndex) in selectedPlan.route" :key="locIndex">
<span class="bg-white px-3 py-2 rounded text-base font-medium whitespace-nowrap">{{ location }}</span>
<i v-if="locIndex < selectedPlan.route.length - 1" class="fas fa-arrow-right text-[#9EC6F3] text-lg flex-shrink-0"></i>
</template>
</div>
</div>
<!-- Daily Plans -->
<div class="w-4/5 mx-auto pb-6">
<h2 class="text-2xl font-bold mb-4 text-[#9FB3DF]">일차별 여행 계획</h2>
<div class="space-y-6">
<div v-for="(day, dayIndex) in selectedPlan.dailyPlans" :key="dayIndex" class="bg-white rounded-lg shadow-md p-6">
<h3 class="text-xl font-semibold mb-4 text-[#9FB3DF] border-b pb-2">Day {{ dayIndex + 1 }}</h3>
<div class="space-y-4">
<div v-for="(activity, actIndex) in day.activities" :key="actIndex" class="flex gap-4 p-3 rounded-md hover:bg-gray-50">
<div class="w-1/6">
<div class="text-sm text-gray-500">{{ formatTime(activity.startTime) }} - {{ formatTime(activity.endTime) }}</div>
</div>
<div class="flex-grow">
<div class="flex items-center gap-2 mb-1">
<h4 class="font-medium text-lg">{{ activity.place }}</h4>
<span class="bg-[#BDDDE4] text-gray-700 px-2 py-1 rounded text-xs">{{ activity.category }}</span>
</div>
<p class="text-gray-600">{{ activity.description }}</p>
</div>
<div class="w-1/6">
<div class="h-16 w-16 rounded-md overflow-hidden">
<img
:src="activity.imageUrl"
:alt="activity.place"
class="h-full w-full object-cover object-top"
/>
</div>
</div>
</div>
</div>
</div>
</div>
<!-- Plan Description -->
<div class="bg-white rounded-lg shadow-md p-6 mt-6">
<h3 class="text-xl font-semibold mb-4 text-[#9FB3DF]">여행 설명</h3>
<p class="text-gray-700 whitespace-pre-line">{{ selectedPlan.description }}</p>
</div>
<!-- Comments Section -->
<div class="bg-white rounded-lg shadow-md p-6 mt-6">
<h3 class="text-xl font-semibold mb-4 text-[#9FB3DF]">댓글 ({{ comments.length }})</h3>
<!-- Comment Form -->
<div class="mb-6 bg-gray-50 p-4 rounded-md">
<textarea
v-model="newComment"
placeholder="댓글을 작성해주세요..."
class="w-full p-3 border-none rounded-md bg-white text-sm focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] min-h-[100px]"
></textarea>
<div class="flex justify-end mt-2">
<button
@click="addComment"
class="bg-[#9FB3DF] text-white px-4 py-2 rounded-button cursor-pointer whitespace-nowrap"
>
댓글 작성
</button>
</div>
</div>
<!-- Comments List -->
<div class="space-y-4">
<div v-for="(comment, index) in comments" :key="index" class="border-b pb-4">
<div class="flex justify-between items-start">
<div class="flex items-center gap-3">
<div class="w-10 h-10 bg-[#FFF1D5] rounded-full flex items-center justify-center">
<i class="fas fa-user text-[#9FB3DF]"></i>
</div>
<div>
<div class="font-medium">{{ comment.author }}</div>
<div class="text-xs text-gray-500">{{ comment.date }}</div>
</div>
</div>
<div v-if="comment.author === currentUser" class="flex gap-2">
<button
v-if="editingCommentId !== comment.id"
@click="startEditComment(comment)"
class="text-[#9FB3DF] hover:text-[#9EC6F3] cursor-pointer whitespace-nowrap"
>
<i class="fas fa-edit"></i> 수정
</button>
<button
@click="deleteComment(comment.id)"
class="text-red-500 hover:text-red-600 cursor-pointer whitespace-nowrap"
>
<i class="fas fa-trash"></i> 삭제
</button>
</div>
</div>
<div class="mt-2 pl-12">
<div v-if="editingCommentId === comment.id">
<textarea
v-model="editCommentText"
class="w-full p-3 border-none rounded-md bg-white text-sm focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] min-h-[80px]"
></textarea>
<div class="flex justify-end gap-2 mt-2">
<button
@click="cancelEditComment"
class="bg-gray-300 text-gray-700 px-3 py-1 rounded-button cursor-pointer whitespace-nowrap"
>
취소
</button>
<button
@click="saveEditComment(comment.id)"
class="bg-[#9FB3DF] text-white px-3 py-1 rounded-button cursor-pointer whitespace-nowrap"
>
저장
</button>
</div>
</div>
<p v-else class="text-gray-700">{{ comment.content }}</p>
</div>
</div>
</div>
<!-- No Comments Message -->
<div v-if="comments.length === 0" class="text-center py-6 text-gray-500">
아직 댓글이 없습니다. 첫 댓글을 작성해보세요!
</div>
</div>
</div>
</div>
</div>
</template>
<script lang="ts" setup>
import { ref, computed } from 'vue';
// Sample data for selected plan
const selectedPlan = ref({
id: 1,
title: '제주도 3박 4일 여행',
region: '제주도',
author: '여행좋아',
route: ['제주공항', '성산일출봉', '우도', '한라산', '서귀포'],
category: '가족 여행',
favorite: false,
description: '제주도의 아름다운 자연을 만끽할 수 있는 3박 4일 여행 계획입니다. 가족과 함께 즐길 수 있는 다양한 관광지와 맛집을 포함했습니다. 특히 성산일출봉에서의 일출과 한라산 등반은 꼭 경험해보세요.\n\n우도에서는 자전거를 빌려 섬을 한 바퀴 돌아보는 것을 추천합니다. 서귀포에서는 해변에서 휴식을 취하며 여행의 마지막을 장식하세요.',
dailyPlans: [
{
activities: [
{
place: '제주공항',
category: '이동',
startTime: '10:00',
endTime: '11:00',
description: '제주공항 도착 후 렌트카 수령',
imageUrl: 'https://readdy.ai/api/search-image?query=Jeju%20International%20Airport%20terminal%20with%20modern%20architecture%2C%20bright%20and%20spacious%20interior%20with%20check-in%20counters%20and%20travelers%2C%20clean%20minimalist%20design%20with%20large%20windows%20showing%20blue%20sky%2C%20high-quality%20professional%20photography&width=200&height=200&seq=1&orientation=squarish'
},
{
place: '제주 흑돼지 맛집',
category: '식사',
startTime: '12:00',
endTime: '13:30',
description: '제주 흑돼지 전문점에서 점심 식사',
imageUrl: 'https://readdy.ai/api/search-image?query=Korean%20BBQ%20restaurant%20with%20Jeju%20black%20pork%20being%20grilled%20on%20table%2C%20steam%20rising%2C%20side%20dishes%20arranged%20neatly%2C%20warm%20lighting%2C%20cozy%20atmosphere%2C%20high-quality%20food%20photography&width=200&height=200&seq=2&orientation=squarish'
},
{
place: '성산일출봉',
category: '관광',
startTime: '14:30',
endTime: '17:00',
description: '성산일출봉 등반 및 주변 관광',
imageUrl: 'https://readdy.ai/api/search-image?query=Seongsan%20Ilchulbong%20Peak%20in%20Jeju%20Island%20with%20dramatic%20volcanic%20crater%20formation%2C%20green%20grass%20covering%20slopes%2C%20blue%20ocean%20surrounding%2C%20clear%20blue%20sky%2C%20tourists%20hiking%20up%20the%20trail%2C%20professional%20landscape%20photography&width=200&height=200&seq=3&orientation=squarish'
},
{
place: '제주 시내 호텔',
category: '숙박',
startTime: '18:00',
endTime: '21:00',
description: '호텔 체크인 및 저녁 식사',
imageUrl: 'https://readdy.ai/api/search-image?query=Modern%20hotel%20lobby%20in%20Jeju%20with%20contemporary%20furniture%2C%20warm%20lighting%2C%20reception%20desk%2C%20elegant%20interior%20design%20with%20subtle%20Jeju-inspired%20elements%2C%20clean%20and%20spacious%20environment%2C%20professional%20hotel%20photography&width=200&height=200&seq=4&orientation=squarish'
}
]
},
{
activities: [
{
place: '우도',
category: '관광',
startTime: '09:00',
endTime: '15:00',
description: '우도 페리 탑승 및 우도 자전거 여행',
imageUrl: 'https://readdy.ai/api/search-image?query=Udo%20Island%20in%20Jeju%20with%20white%20sandy%20beach%2C%20crystal%20clear%20turquoise%20water%2C%20small%20ferry%20approaching%20the%20dock%2C%20green%20hills%20in%20background%2C%20bright%20sunny%20day%2C%20people%20riding%20bicycles%20along%20coastal%20path%2C%20professional%20travel%20photography&width=200&height=200&seq=5&orientation=squarish'
},
{
place: '제주 해산물 식당',
category: '식사',
startTime: '15:30',
endTime: '17:00',
description: '신선한 제주 해산물로 저녁 식사',
imageUrl: 'https://readdy.ai/api/search-image?query=Fresh%20seafood%20platter%20in%20Jeju%20restaurant%20with%20variety%20of%20shellfish%2C%20fish%2C%20and%20local%20specialties%2C%20beautifully%20arranged%20on%20ice%2C%20garnished%20with%20lemon%20and%20herbs%2C%20wooden%20table%20setting%2C%20coastal%20view%20through%20window%2C%20professional%20food%20photography&width=200&height=200&seq=6&orientation=squarish'
},
{
place: '제주 야시장',
category: '쇼핑',
startTime: '19:00',
endTime: '21:00',
description: '제주 야시장 방문 및 쇼핑',
imageUrl: 'https://readdy.ai/api/search-image?query=Jeju%20night%20market%20with%20colorful%20food%20stalls%2C%20local%20vendors%20selling%20crafts%20and%20souvenirs%2C%20string%20lights%20overhead%2C%20crowds%20of%20shoppers%20browsing%2C%20street%20food%20being%20prepared%2C%20warm%20evening%20atmosphere%2C%20professional%20street%20photography&width=200&height=200&seq=7&orientation=squarish'
}
]
},
{
activities: [
{
place: '한라산',
category: '등산',
startTime: '07:00',
endTime: '14:00',
description: '한라산 등반 (백록담 코스)',
imageUrl: 'https://readdy.ai/api/search-image?query=Hallasan%20Mountain%20in%20Jeju%20with%20hiking%20trail%20leading%20to%20Baengnokdam%20crater%20lake%2C%20lush%20green%20vegetation%2C%20volcanic%20rock%20formations%2C%20hikers%20on%20path%2C%20clear%20view%20of%20summit%2C%20blue%20sky%20with%20few%20clouds%2C%20professional%20nature%20photography&width=200&height=200&seq=8&orientation=squarish'
},
{
place: '오설록 티 뮤지엄',
category: '관광',
startTime: '15:30',
endTime: '17:00',
description: '오설록 티 뮤지엄 방문 및 녹차 체험',
imageUrl: 'https://readdy.ai/api/search-image?query=Osulloc%20Tea%20Museum%20in%20Jeju%20with%20green%20tea%20plantation%20surrounding%20modern%20architecture%20building%2C%20neatly%20arranged%20tea%20bushes%20in%20rows%2C%20visitors%20enjoying%20tea%20ceremony%20inside%2C%20green%20tea%20desserts%20being%20served%2C%20professional%20architectural%20photography&width=200&height=200&seq=9&orientation=squarish'
},
{
place: '제주 흑돼지 BBQ',
category: '식사',
startTime: '18:30',
endTime: '20:30',
description: '제주 흑돼지 BBQ 저녁 식사',
imageUrl: 'https://readdy.ai/api/search-image?query=Korean%20BBQ%20restaurant%20with%20Jeju%20black%20pork%20being%20grilled%20on%20table%2C%20steam%20rising%2C%20side%20dishes%20arranged%20neatly%2C%20warm%20lighting%2C%20cozy%20atmosphere%2C%20high-quality%20food%20photography&width=200&height=200&seq=10&orientation=squarish'
}
]
},
{
activities: [
{
place: '서귀포',
category: '관광',
startTime: '09:00',
endTime: '11:00',
description: '서귀포 해변 산책 및 휴식',
imageUrl: 'https://readdy.ai/api/search-image?query=Seogwipo%20coastline%20in%20Jeju%20with%20black%20volcanic%20rocks%2C%20blue%20ocean%20waves%20crashing%2C%20palm%20trees%20lining%20promenade%2C%20people%20walking%20along%20coastal%20path%2C%20clear%20blue%20sky%2C%20mountains%20in%20background%2C%20professional%20landscape%20photography&width=200&height=200&seq=11&orientation=squarish'
},
{
place: '중문 관광단지',
category: '관광',
startTime: '11:30',
endTime: '14:00',
description: '중문 관광단지 방문 및 점심 식사',
imageUrl: 'https://readdy.ai/api/search-image?query=Jungmun%20Tourist%20Complex%20in%20Jeju%20with%20tropical%20gardens%2C%20waterfalls%2C%20luxury%20hotels%20visible%20in%20background%2C%20tourists%20walking%20on%20paths%2C%20souvenir%20shops%2C%20restaurant%20terraces%20with%20ocean%20view%2C%20sunny%20day%2C%20professional%20resort%20photography&width=200&height=200&seq=12&orientation=squarish'
},
{
place: '제주공항',
category: '이동',
startTime: '16:00',
endTime: '18:00',
description: '렌트카 반납 및 공항 이동, 귀가',
imageUrl: 'https://readdy.ai/api/search-image?query=Jeju%20International%20Airport%20departure%20hall%20with%20check-in%20counters%2C%20passengers%20with%20luggage%2C%20departure%20board%2C%20modern%20interior%20design%2C%20large%20windows%2C%20clean%20and%20organized%20space%2C%20professional%20airport%20photography&width=200&height=200&seq=13&orientation=squarish'
}
]
}
]
});
// Format time helper
const formatTime = (timeString: string) => {
const [hours, minutes] = timeString.split(':');
return `${hours}:${minutes}`;
};
// Toggle favorite
const toggleFavorite = (id: number) => {
selectedPlan.value.favorite = !selectedPlan.value.favorite;
};
// Go back to list
const goBackToList = () => {
// In a real app, this would navigate back to the list page
alert('목록으로 돌아가기 기능은 실제 앱에서 구현됩니다.');
};
// Comments functionality
const currentUser = '여행자123';
const newComment = ref('');
const editingCommentId = ref<number | null>(null);
const editCommentText = ref('');
const comments = ref([
{
id: 1,
author: '여행자123',
content: '정말 좋은 여행 계획이네요! 저도 이 코스대로 가볼 계획입니다.',
date: '2025-05-20'
},
{
id: 2,
author: '제주도사랑',
content: '성산일출봉은 꼭 일출 시간에 가보세요. 정말 장관입니다.',
date: '2025-05-21'
},
{
id: 3,
author: '맛집탐험가',
content: '제주 흑돼지 맛집 추천해주세요! 어디가 제일 맛있나요?',
date: '2025-05-22'
}
]);
const addComment = () => {
if (newComment.value.trim() === '') return;
const today = new Date();
const year = today.getFullYear();
const month = String(today.getMonth() + 1).padStart(2, '0');
const day = String(today.getDate()).padStart(2, '0');
comments.value.unshift({
id: comments.value.length + 1,
author: currentUser,
content: newComment.value,
date: `${year}-${month}-${day}`
});
newComment.value = '';
};
const startEditComment = (comment: { id: number; content: string }) => {
editingCommentId.value = comment.id;
editCommentText.value = comment.content;
};
const cancelEditComment = () => {
editingCommentId.value = null;
editCommentText.value = '';
};
const saveEditComment = (id: number) => {
if (editCommentText.value.trim() === '') return;
const commentIndex = comments.value.findIndex(comment => comment.id === id);
if (commentIndex !== -1) {
comments.value[commentIndex].content = editCommentText.value;
cancelEditComment();
}
};
const deleteComment = (id: number) => {
if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
comments.value = comments.value.filter(comment => comment.id !== id);
}
};
</script>
<style scoped>
/* Hide number input arrows */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
-webkit-appearance: none;
margin: 0;
}
input[type="number"] {
-moz-appearance: textfield;
}
/* Custom scrollbar */
::-webkit-scrollbar {
width: 8px;
}
::-webkit-scrollbar-track {
background: #f1f1f1;
border-radius: 10px;
}
::-webkit-scrollbar-thumb {
background: #9FB3DF;
border-radius: 10px;
}
::-webkit-scrollbar-thumb:hover {
background: #9EC6F3;
}
</style>