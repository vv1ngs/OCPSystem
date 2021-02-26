import request from '@/utils/request'
import qs from 'qs'
export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

export function getRoles() {
  return request({
    url: '/vue-element-admin/roles',
    method: 'get'
  })
}

export function addRole(name, describe) {
  return request({
    url: '/role/addRole',
    method: 'post',
    data: qs.stringify({ 'name': name, 'describe': describe })
  })
}
export function updateRole(id, name, describe) {
  return request({
    url: 'role/updateRole',
    method: 'post',
    data: qs.stringify({ 'id': id, 'name': name, 'describe': describe })
  })
}
export function delRole(rid) {
  return request({
    url: '/role/delRole?rid=' + rid.join(),
    method: 'get'
  })
}
export function startOrStopRole(data, flag) {
  return request({
    url: '/roleOrStopRole?rid=' + data.join() + '&flag=' + flag,
    method: 'get'
  })
}
export function associatedAuthority(data, data2) {
  return request({
    url: '/role/associatedAuthority?rid=' + data.join() + '&aid=' + data2.join(),
    method: 'get'
    // params: { data, data2 },
    // paramsSerializer: params => {
    //   return qs.stringify(params, { arrayFormat: 'repeat' })
    // }
  })
}
export function getAssociatedAuthority(rid) {
  return request({
    url: 'role/getAssociatedAuthority',
    method: 'post',
    data: qs.stringify({ 'rid': rid })
    // params: { data, data2 },
    // paramsSerializer: params => {
    //   return qs.stringify(params, { arrayFormat: 'repeat' })
    // }
  })
}
export function getAllAuthority(data, flag) {
  return request({
    url: '/role/getAllAuthority',
    method: 'get'
  })
}

export function searchRole(username, status) {
  return request({
    url: '/role/searchRole',
    method: 'post',
    data: qs.stringify({ 'name': username, 'status': status })
  })
}
export function fetchRole() {
  return request({
    url: `/role/getAllRole`,
    method: 'get'
  })
}

