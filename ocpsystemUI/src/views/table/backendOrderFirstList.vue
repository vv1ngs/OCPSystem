<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.dealerName" placeholder="经销商名称" style="width: 250px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.dealerId" placeholder="经销商编码" style="width: 250px;" class="filter-item" />
      <el-input v-model="listQuery.orderId" placeholder="订单编码" style="width: 250px;" class="filter-item" />
      <el-input v-model="listQuery.startAmount" placeholder="起始金额" style="width: 250px;" class="filter-item"/>
      <el-input v-model="listQuery.endAmount" placeholder="终止金额" style="width: 250px;" class="filter-item"/>

      <!--<el-select v-model="listQuery.status" placeholder="状态" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in statusOptions" :key="item" :label="item.label" :value="item.value" />
      </el-select>-->

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="orderList"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
      <el-table-column label="下单日期" prop="createTime" sortable="custom" align="center" width="100" />
      <el-table-column label="经销商名称" prop="dealerName" sortable="custom" align="center" width="100" />
      <el-table-column label="经销商编码" prop="dealerId" sortable="custom" align="center" width="100" />
      <el-table-column label="销售订单号" prop="orderId" sortable="custom" align="center" width="144" />
      <el-table-column label="订单数量" prop="totalCount" sortable="custom" align="center" width="100" />
      <el-table-column label="订单金额" prop="totalAmount" sortable="custom" align="center" width="100" />
      <el-table-column label="区域" prop="area" sortable="custom" align="center" width="100" />
      <el-table-column label="不通过原因" prop="productOpenFare" sortable="custom" align="center" width="100" />
      <el-table-column label="状态" prop="status" sortable="custom" align="center" width="100" />
      <el-table-column label="订单体积" prop="totalVolume" sortable="custom" align="center" width="100" />
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button size="mini" type="danger" @click="handlerCheck(row)">
            审核订单
          </el-button>
          <el-button size="mini" type="success" @click="showProduct(row)">
            查看订单产品
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realname">
          <el-input v-model="temp.realname" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="temp.sex" class="filter-item" placeholder="Please select">
            <el-option v-for="item in sexList" :key="item" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="temp.phone" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="temp.email" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.note" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog title="用户角色" :visible.sync="dialogRoleVisible">
      <el-form ref="dataForm" :rules="rules" :model="userRole" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-tag v-for="item in userRole" :key="item">{{ item.name }}</el-tag>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogCheckRoleVisible1" title="权限表">
      <el-tree
        ref="tree"
        :data="categoryList"
        :props="AuthorityLabel"
        node-key="path"
        class="permission-tree"
      />
    </el-dialog>
    <el-dialog :visible.sync="dialogProductVisible" title="订单商品表" width="1000px" @close="resetShowProductList">
      <el-table :data="showProductList" border fit width="3000px">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
        <el-table-column label="产品编码" prop="productId" sortable="custom" align="center" width="120" />
        <el-table-column label="产品型号" prop="productModel" sortable="custom" align="center" width="100" />
        <el-table-column label="产品名称" prop="productName" sortable="custom" align="center" width="120" />
        <el-table-column label="产品体积" prop="productVolume" sortable="custom" align="center" width="100" />
        <el-table-column label="产品价格" prop="productOpenFare" sortable="custom" align="center" width="100" />
        <el-table-column label="产品数量" prop="count" sortable="custom" align="center" width="100" />
      </el-table>
    </el-dialog>
    <el-dialog title="审核订单" :visible.sync="outerVisible" center>
      <el-dialog
        width="30%"
        title="提供原因"
        :visible.sync="innerVisible"
        append-to-body
        center>
        <el-form ref="dataForm" :rules="rules" :model="temp" >
          <el-form-item label="原因">
            <el-input v-model="temp.reason" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
          </el-form-item>
            <el-button type="primary" @click="checkorder(9)">
              确认
            </el-button>
        </el-form>
      </el-dialog>
      <div slot="footer" class="dialog-footer">
        <el-button @click="checkorder(3)">审核通过</el-button>
        <el-button type="primary" @click="innerVisible = true">审核不通过</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchPv, fetchRoleList } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { searchOrder, verifyOrder, getAllOrderByStatus, addOrder, getAllProduct, getAllDealerId } from '@/api/dealerOrder'
import { getChildCategory, getAllCategory, delDealer, startOrStopDealer, searchChildDealer, updateDealer, addChildDealer, getAllChildDealer } from '@/api/childManage'
import { getAllAuthority } from '@/api/myrole'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import Moment from 'moment'

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      outerVisible: false,
      innerVisible: false,
      AuthorityLabel: {
        label: 'name'
      },
      dialogCheckRoleVisible1: false,
      dialogCheckRoleVisible: true,
      categoryList: [],
      multipleSelection: [],
      RoleAuthority: [],
      AuthoritySelection: [],
      tableKey: 0,
      list: [],
      AuthorityList: [],
      SelectedAuthority: [],
      total: 0,
      roleList: [],
      userRole: [],
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        sex: '',
        title: undefined,
        name: '',
        realname: '',
        status: '',
        phone: '',
        email: '',
        orderId: '',
        dealerName: '',
        dealerId: '',
        startAmount: 0,
        endAmount: 0
      },
      checkRoleList: [],
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: [{ label: '启用', value: 'true' }, { label: '禁用', value: 'false' }],
      sexList: [{ label: '男', value: true }, { label: '女', value: false }],
      showReviewer: false,
      temp: {
        id: undefined,
        describe: '',
        name: '',
        institutionId: 0,
        realname: '',
        role: '',
        title: '',
        sex: true,
        status: true,
        phone: '',
        email: '',
        reason: ''
      },
      dialogFormVisible: false,
      dialogRoleVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        username: [{ required: true, message: 'type is required', trigger: 'blur' }],
        realname: [{ required: true, message: 'title is required', trigger: 'blur' }],
        sex: [{ required: true, message: 'title is required', trigger: 'blur' }]

      },
      downloadLoading: false,
      DealerList: [],
      checkList: undefined,
      productList: [],
      dialogProductVisible: false,
      count: 0,
      countList: [],
      productIdList: [],
      showProductList: [],
      totalCount: 0,
      totalVolume: 0,
      totalAmount: 0,
      value1: '',
      value2: '',
      value3: '',
      weekVal: '',
      orderList: [],
      checkOrderId: ''
    }
  },
  created() {
    // this.getAllDealerId()
    // this.getList()
    // this.getCategory()
    this.getAllOrder()
  },
  methods: {
    checkorder(status) {
      verifyOrder(this.checkOrderId, status, this.temp.reason).then(() => {
        if (status === 3) {
          this.outerVisible = false
        } else if (status === 9) {
          this.innerVisible = false
        }
        this.getAllOrder()
      })
    },
    resetShowProductList() {
      this.showProductList = []
    },
    handlerCheck(row) {
      this.outerVisible = true
      this.checkOrderId = row.id
    },
    showProduct(row) {
      this.dialogProductVisible = true
      this.showProductList = []
      for (let j = 0; j < row.list.length; j++) {
        this.showProductList.push(row.list[j].productInfo)
        this.showProductList.map(v => {
          this.$set(v, 'count', row.list[j].count)
        })
      }
    },
    getAllOrder() {
      this.listLoading = true
      getAllOrderByStatus(2).then((response) => {
        this.orderList = response.data
        for (let i = 0; i < this.orderList.length; i++) {
          for (let j = 0; j < this.orderList[i].list.length; j++) {
            this.showProductList.push(this.orderList[i].list[j].productInfo)
            this.showProductList.map(v => {
              this.$set(v, 'count', this.orderList[i].list[j].count)
            })
          }

          if (this.orderList[i].status === 0) {
            response.data[i].status = '已取消'
          } else if (this.orderList[i].status === 1) {
            response.data[i].status = '未提交'
          } else if (this.orderList[i].status === 2) {
            response.data[i].status = '已经提交等待初核实'
          } else if (this.orderList[i].status === 3) {
            response.data[i].status = '待上传附件'
          } else if (this.orderList[i].status === 4) {
            response.data[i].status = '已上传附件等待复核'
          } else if (this.orderList[i].status === 5) {
            response.data[i].status = '驳回等待再次上传'
          } else if (this.orderList[i].status === 6) {
            response.data[i].status = '复核通过'
          } else if (this.orderList[i].status === 7) {
            response.data[i].status = '已发货'
          } else if (this.orderList[i].status === 8) {
            response.data[i].status = '确认收货'
          } else if (this.orderList[i].status === 9) {
            response.data[i].status = '初次驳回请重新编辑后提交'
          }
        }
        this.listLoading = false
      })
    },
    submitOrder(status) {
      const pid = []
      const cid = []
      this.setWeekNum()
      for (let i = 0; i < this.showProductList.length; i++) {
        pid.push(this.showProductList[i].id)
        cid.push(this.showProductList[i].count)
      }
      addOrder(this.checkList, this.weekVal, this.value2, this.value3, pid, cid, status)
    },
    setWeekNum() {
      // 注意，当返回值是中国标准时间的格式时使用moment.js的时候一定要加上.utcOffset(480)，用来转换时区
      this.weekVal = Moment(this.value1).utcOffset(480).format('W')
      console.log(this.weekVal)
    },
    addProductToShowList(row) {
      this.showProductList.push(row)
      this.productIdList.push(row.id)
      this.count = 0
      this.totalAmount += row.productOpenFare * row.count
      this.totalCount += row.count
      this.totalVolume += row.productVolume
      this.dialogProductVisible = false
    },
    addProduct() {
      this.dialogProductVisible = true
    },
    changeDeal(val) {
      this.checkList.push(val)
    },
    getAllDealerId() {
      getAllDealerId().then((response) => {
        this.DealerList = response.data
      })
    },
    getCategory() {
      getAllCategory().then((response) => {
        this.categoryList = response.data
      })
    },
    getAuthorityList() {
      getAllAuthority().then((response) => {
        this.AuthorityList = response.data
      })
    },
    handleSelectionChange(val) {
      for (let i = 0; i < val.length; i++) {
        this.multipleSelection.push(val[i].id)
      }
    },
    handleSelectionChange2(val) {
      for (let i = 0; i < val.length; i++) {
        this.AuthoritySelection.push(val[i].id)
      }
    },
    startOrStop(flag) {
      startOrStopDealer(this.multipleSelection, flag).then(() => {
        console.log(flag.type)
        for (let i = this.list.length; i > 0; i--) {
          for (let j = 0; j < this.multipleSelection.length; j++) {
            if (
              this.list[i - 1].id ===
              this.multipleSelection[j]
            ) {
              console.log(this.list)
              console.log(i)
              if (flag === true) {
                this.list[i - 1].status = '启用'
              }
              if (flag === false) {
                this.list[i - 1].status = '禁用'
              }
              if (this.temp.sex === false) {
                this.temp.sex = '女'
              }
              if (this.temp.sex === true) {
                this.temp.sex = '男'
              }
            }
          }
        }
      })
      this.$notify({
        title: 'Success',
        message: 'update Successfully',
        type: 'success',
        duration: 2000
      })
    },
    getRoleList() {
      this.listLoading = true
      fetchRoleList().then(response => {
        this.roleList = response.data
        console.log(this.roleList[0].name)
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    getList() {
      this.listLoading = true
      getAllChildDealer(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.data.length
        for (let i = 0; i < this.total; i++) {
          if (response.data[i].status === true) {
            response.data[i].status = '启用'
          }
          if (response.data[i].status === false) {
            response.data[i].status = '禁用'
          }
          if (response.data[i].sex === true) {
            response.data[i].sex = '男'
          }
          if (response.data[i].sex === false) {
            response.data[i].sex = '女'
          }
        }

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      searchOrder(this.listQuery.dealerName, this.listQuery.dealerId, this.listQuery.orderId, this.listQuery.startAmount, this.listQuery.endAmount,2).then((response) => {
        this.orderList = response.data
        for (let i = 0; i < this.orderList.length; i++) {
          for (let j = 0; j < this.orderList[i].list.length; j++) {
            this.showProductList.push(this.orderList[i].list[j].productInfo)
            this.showProductList.map(v => {
              this.$set(v, 'count', this.orderList[i].list[j].count)
            })
          }

          if (this.orderList[i].status === 0) {
            response.data[i].status = '已取消'
          } else if (this.orderList[i].status === 1) {
            response.data[i].status = '未提交'
          } else if (this.orderList[i].status === 2) {
            response.data[i].status = '已经提交等待初核实'
          } else if (this.orderList[i].status === 3) {
            response.data[i].status = '待上传附件'
          } else if (this.orderList[i].status === 4) {
            response.data[i].status = '已上传附件等待复核'
          } else if (this.orderList[i].status === 5) {
            response.data[i].status = '驳回等待再次上传'
          } else if (this.orderList[i].status === 6) {
            response.data[i].status = '复核通过'
          } else if (this.orderList[i].status === 7) {
            response.data[i].status = '已发货'
          } else if (this.orderList[i].status === 8) {
            response.data[i].status = '确认收货'
          } else if (this.orderList[i].status === 9) {
            response.data[i].status = '初次驳回请重新编辑后提交'
          }
        }
        this.listLoading = false
      })
    },
    handleAllocationRole(row) {
      this.resetRole()
      this.temp = Object.assign({}, row) // copy obj
      this.dialogCheckRoleVisible = true
    },
    AllocationCategory() {
      console.log(this.checkList)
      if (this.checkList !== undefined) {
        this.dialogCheckRoleVisible = false
        getAllProduct(this.checkList).then((response) => {
          this.productList = response.data
          this.productList.map(v => {
            this.$set(v, 'count', 0)
          })
        })
      } else {
        this.dialogCheckRoleVisible = true
      }
    },
    showCategory(row) {
      getChildCategory(row.id).then(response => {
        this.RoleAuthority = response.data
        this.dialogCheckRoleVisible1 = true
      })
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        role: '',
        institutionId: 0,
        title: '',
        status: true,
        sex: true,
        phone: '',
        email: ''
      }
    },
    resetRole() {
      this.checkRoleList = []
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.institutionId = 0
          addChildDealer(this.temp).then(() => {
            this.list.push(this.temp)
            if (this.temp.status === false) {
              this.temp.status = '禁用'
            }
            if (this.temp.status === true) {
              this.temp.status = '启用'
            }
            if (this.temp.sex === false) {
              this.temp.sex = '女'
            }
            if (this.temp.sex === true) {
              this.temp.sex = '男'
            }
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      console.log(row.id)
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp)
          if (tempData.status === '禁用') {
            tempData.status = 'false'
          }
          if (tempData.status === '启用') {
            tempData.status = 'true'
          }
          if (tempData.sex === '女') {
            tempData.sex = 'false'
          }
          if (tempData.sex === '男') {
            tempData.sex = 'true'
          }// change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateDealer(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    delAllSelection() {
      console.log(this.multipleSelection)
      delDealer([this.multipleSelection]).then(() => {
        for (let i = this.list.length; i > 0; i--) {
          for (let j = 0; j < this.multipleSelection.length; j++) {
            console.log(this.list[i - 1].id)
            console.log(this.list)
            if (
              this.list[i - 1].id ===
              this.multipleSelection[j]
            ) {
              this.list.splice(i - 1, 1)
            }
          }
        }
      })
      this.$notify({
        title: 'Success',
        message: 'Delete Successfully',
        type: 'success',
        duration: 2000
      })
    },
    handleDelete(row, index) {
      this.totalAmount -= row.productOpenFare * row.count
      this.totalCount -= row.count
      this.totalVolume -= row.productVolume
      this.showProductList.splice(index, 1)
    },
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>
