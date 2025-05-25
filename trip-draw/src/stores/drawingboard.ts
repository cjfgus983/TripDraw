// src/stores/drawingboard.ts
import { defineStore } from 'pinia'
import axios from 'axios'

export interface BoardItem {
  drawingBoardId: number
  drawingId:      number
  userId:         number
  nickname:       string
  title:          string
  content:        string | null
  viewCount:      number
  likeCount:      number
  createdAt:      string
  imageUrl:       string
  isLiked:        boolean
}

export const useDrawingBoardStore = defineStore('drawingboard', {
  state: () => ({
    artworks: [] as BoardItem[],      // 전체 그림 목록
    popular:  [] as BoardItem[],      // 이번 주 인기 그림
    page:     1                      // 현재 로드된 페이지 번호
  }),
  actions: {
    /**
     * 인기 작품과 전체 작품을 동시에 가져옵니다.
     * @param page 불러올 페이지 번호 (기본 1)
     */
    async fetchAll(page = 1) {
      this.page = page
      const [popRes, allRes] = await Promise.all([
        axios.get<BoardItem[]>(`/api/drawingboard/popular`),
        axios.get<BoardItem[]>(`/api/drawingboard?page=${page}`)
      ])
      this.popular  = popRes.data
      this.artworks = allRes.data
    },

    /**
     * 더보기 버튼을 눌러 다음 페이지를 로드합니다.
     */
    async loadMore() {
      const next = this.page + 1
      const res  = await axios.get<BoardItem[]>(`/api/drawingboard?page=${next}`)
      this.artworks.push(...res.data)
      this.page = next
    },

    /**
     * 좋아요 토글 처리 후 전체와 인기 배열을 동기화합니다.
     * @param boardId 좋아요를 토글할 drawingBoardId
     */
    async toggleLike(boardId: number) {
      // 대상 아이템 참조
      const targetAll = this.artworks.find(a => a.drawingBoardId === boardId)
      const targetPop = this.popular .find(a => a.drawingBoardId === boardId)
      if (!targetAll) return

      // 반전될 상태와 증감량
      const willLike = !targetAll.isLiked
      const delta    = willLike ? 1 : -1

      // API 호출
      if (willLike) await axios.post(`/api/drawingboard/${boardId}/like`)
      else         await axios.delete(`/api/drawingboard/${boardId}/like`)

      // 상태 갱신
      targetAll.isLiked   = willLike
      targetAll.likeCount += delta
      if (targetPop) {
        targetPop.isLiked   = willLike
        targetPop.likeCount += delta
      }
    },

    /**
     * 특정 게시물을 삭제합니다.
     * @param boardId 삭제할 drawingBoardId
     */
    async deleteArtwork(boardId: number) {
      await axios.delete(`/api/drawingboard/${boardId}`)
      this.artworks = this.artworks.filter(a => a.drawingBoardId !== boardId)
      this.popular  = this.popular .filter(a => a.drawingBoardId !== boardId)
    }
  }
})
