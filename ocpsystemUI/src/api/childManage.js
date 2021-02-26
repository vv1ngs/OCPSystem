import request from '@/utils/request'
import qs from 'qs'
export function getAllChildDealer() {
  return request({
    url: `/dealerManagement/getAllChildDealer`,
    method: 'get'
  })
}
export function getAllCategory() {
  return request({
    url: `/dealerManagement/getAllCategory`,
    method: 'get'
  })
}
export function searchChildDealer(data) {
  return request({
    url: `/dealerManagement/searchChildDealer`,
    method: 'post',
    data
  })
}
export function addChildDealer(data) {
  return request({
    url: `/dealerManagement/addChildDealer`,
    method: 'post',
    data
  })
}
export function delDealer(data) {
  return request({
    url: `/dealerManagement/delDealer?uid=` + data.join(),
    method: 'get'
  })
}
export function updateDealer(data) {
  return request({
    url: `/dealerManagement/updateDealer`,
    method: 'post',
    data
  })
}
export function startOrStopDealer(data, flag) {
  return request({
    url: `/dealerManagement/startOrStopDealer?uid=` + data.join() + '&flag=' + flag,
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

export function assignCategory(data, data2) {
  return request({
    url: `/dealerManagement/assignCategory?uid=` + data.join() + '&cid=' + data2.join(),
    method: 'get'
  })
}

export function removeAssociatedDealer(dealerId, manageId) {
  return request({
    url: `/managerDealer/removeAssociatedDealer`,
    method: 'post',
    data: qs.stringify({ 'dealerId': dealerId, 'manageId': manageId })
  })
}
export function getChildCategory(childId) {
  return request({
    url: `/dealerManagement/getChildCategory`,
    method: 'post',
    data: qs.stringify({ 'childId': childId })
  })
}

