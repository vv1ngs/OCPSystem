/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/table',
  component: Layout,
  redirect: '/table/complex-table',
  name: '系统配置',
  meta: {
    title: '系统配置',
    icon: 'table'
  },
  children: [
    {
      path: 'complex-table',
      component: () => import('@/views/table/complex-table'),
      name: '运营账号管理',
      meta: { title: '运营账号管理', roles: ['root'] }
    },
    {
      path: 'role',
      component: () => import('@/views/table/complex-table-role'),
      name: '运营角色管理',
      meta: { title: '运营角色管理', roles: ['root'] }
    },

    {
      path: 'manage',
      component: () => import('@/views/table/manage'),
      name: '运营商账号管理',
      meta: { title: '运营商账号管理', roles: ['root'] }
    }
  ]
}
export default tableRouter
