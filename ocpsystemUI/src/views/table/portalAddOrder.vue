<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="addProduct">
        添加商品
      </el-button>
      <span class="demonstration">选择备货周</span>
      <el-date-picker
        v-model="value1"
        type="week"
        format="第 W 周"
        placeholder="选择周"
      />
      <span class="demonstration">选择备货月</span>
      <el-date-picker
        v-model="value2"
        type="month"
        value-format="M"
        format="第 M 月"
        placeholder="选择月"
      />
      <span class="demonstration">选择备货年</span>
      <el-date-picker
        v-model="value3"
        type="year"
        value-format="yyyy"
        placeholder="选择年"
      />
    </div>
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="showProductList"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
      <el-table-column label="产品编码" prop="productId" sortable="custom" align="center" width="120" />
      <el-table-column label="产品型号" prop="productModel" sortable="custom" align="center" width="100" />
      <el-table-column label="产品名称" prop="productName" sortable="custom" align="center" width="120" />
      <el-table-column label="数目" prop="count" sortable="custom" align="center" width="120" />
      <el-table-column label="开票价" prop="productOpenFare" sortable="custom" align="center" width="120" />
      <el-table-column label="产品体积" prop="productVolume" sortable="custom" align="center" width="100" />
      <el-table-column label="操作" align="center" width="400" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <aside>总数量：{{ totalCount }} 总体积：{{ totalVolume }} 总金额：{{ totalAmount }}</aside>
    <div slot="footer" class="footer">
      <el-button type="primary" @click="submitOrder(2)">
        提交
      </el-button>
      <el-button type="warning" @click="submitOrder(1)">
        保存至处理订单列表
      </el-button>
    </div>
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
    <el-dialog :visible.sync="dialogProductVisible" title="选择商品" width="1000px">
      <el-table :data="productList" border fit width="3000px">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
        <el-table-column label="产品编码" prop="productId" sortable="custom" align="center" width="120" />
        <el-table-column label="产品型号" prop="productModel" sortable="custom" align="center" width="100" />
        <el-table-column label="产品名称" prop="productName" sortable="custom" align="center" width="120" />
        <el-table-column label="产品体积" prop="productVolume" sortable="custom" align="center" width="100" />
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width" prop="associated">
          <template slot-scope="{row}">
            <el-button size="mini" type="primary" @click="addProductToShowList(row)">
              添加
            </el-button>
            <el-input-number v-model="row.count" size="small" :min="1" :max="999" label="描述文字" />
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <el-dialog :visible.sync="dialogCheckRoleVisible" title="选择你要下单的经销商">
      <el-radio-group v-model="checkList" @change="changeDeal">
        <el-radio-button v-for="item in DealerList" :key="item" :label="item.id">{{ item.name }}</el-radio-button>
      </el-radio-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCheckRoleVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="AllocationCategory">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchPv, fetchRoleList } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { addOrder, getAllProduct, getAllDealerId } from '@/api/dealerOrder'
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
        email: ''

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
        email: ''
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
      weekVal: ''
    }
  },
  created() {
    this.getAllDealerId()
    // this.getList()
    // this.getCategory()
  },
  methods: {
    submitOrder(status) {
      const pid = []
      const cid = []
      this.setWeekNum()
      for (let i = 0; i < this.showProductList.length; i++) {
        pid.push(this.showProductList[i].id)
        cid.push(this.showProductList[i].count)
      }
      addOrder(this.checkList, this.weekVal, this.value2, this.value3, pid, cid, status).then(() => {
      })
      this.$notify({
        title: 'Success',
        message: 'update Successfully',
        type: 'success',
        duration: 2000
      })
      this.$router.push(`/portalOrder/OrderList`)
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
      searchChildDealer(this.listQuery).then((response) => {
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
