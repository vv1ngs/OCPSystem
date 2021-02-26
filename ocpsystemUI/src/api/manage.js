import request from '@/utils/request'
import qs from 'qs'
export function getAllManageDealer() {
  return request({
    url: `/managerDealer/searchManageDealer`,
    method: 'post'
  })
}
export function searchAllManageDealer(name, username, status) {
  return request({
    url: `/managerDealer/searchManageDealer`,
    method: 'post',
    data: qs.stringify({ 'name': name, 'username': username, 'status': status })
  })
}
export function addManageDealer(data) {
  return request({
    url: `/managerDealer/addManageDealer`,
    method: 'post',
    data
  })
}
export function delManageDealer(data) {
  return request({
    url: `/managerDealer/delManageDealer?uid=` + data.join(),
    method: 'get'
  })
}
export function updateManageDealer(data) {
  return request({
    url: `/managerDealer/updateManageDealer`,
    method: 'post',
    data
  })
}
export function startOrStopManageDealer(data, flag) {
  return request({
    url: `/managerDealer/startOrStopManageDealer?uid=` + data.join() + '&flag=' + flag,
    method: 'get'
  })
}
export function getAllDealer() {
  return request({
    url: `/managerDealer/getAllDealer`,
    method: 'get'
  })
}
export function getAllAssociatedDealer(id) {
  return request({
    url: `/managerDealer/getAllAssociatedDealer?mid=` + id,
    method: 'get'
  })
}
export function associatedDealer(dealerId, manageId) {
  return request({
    url: `/managerDealer/associatedDealer`,
    method: 'post',
    data: qs.stringify({ 'dealerId': dealerId, 'manageId': manageId })
  })
}
export function removeAssociatedDealer(dealerId, manageId) {
  return request({
    url: `/managerDealer/removeAssociatedDealer`,
    method: 'post',
    data: qs.stringify({ 'dealerId': dealerId, 'manageId': manageId })
  })
}
export function getAllCategory() {
  return request({
    url: `/managerDealer/getAllCategory`,
    method: 'get'
  })
}
