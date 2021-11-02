const router = {
  Analysis: () => import('@/modules/dashboard/views/Analysis.vue'),
  Workplace: () => import('@/modules/dashboard/views/Workplace.vue'),
  Monitor: () => import('@/modules/dashboard/views/Monitor.vue')
}

export default router
