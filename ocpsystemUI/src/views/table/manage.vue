<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="经销商名称" style="width: 250px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.username" placeholder="用户名" style="width: 250px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-select v-model="listQuery.status" placeholder="状态" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in statusOptions" :key="item" :label="item.label" :value="item.value" />
      </el-select>

      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增
      </el-button>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-delete" @click="delAllSelection">
        批量删除
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-" @click="startOrStop(true)">
        批量启用
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-" @click="startOrStop(false)">
        批量禁用
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
      <el-table-column label="用户名" prop="username" sortable="custom" align="center" width="100" />
      <el-table-column label="经销商名称" prop="name" sortable="custom" align="center" width="200" />
      <el-table-column label="联系人" prop="contactName" sortable="custom" align="center" width="120" />
      <el-table-column label="手机号码" prop="phone" sortable="custom" align="center" width="120" />
      <el-table-column label="邮箱" prop="email" sortable="custom" align="center" width="150" />
      <el-table-column label="最后修改人" prop="lastModifier" sortable="custom" align="center" width="100" />
      <el-table-column label="备注" prop="note" sortable="custom" align="center" width="50" />
      <el-table-column label="状态" prop="status" sortable="custom" align="center" width="100" />
      <el-table-column label="操作" align="center" width="400" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑账号
          </el-button>
          <el-button size="mini" @click="handleAssociatedDealerList(row)">
            关联经销商
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" >
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="经销商名称" prop="name">
          <el-input v-model="temp.name" :autosize="{ minRows: 2, maxRows: 4}"  placeholder="Please input" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="temp.contactName" :autosize="{ minRows: 2, maxRows: 4}" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="temp.phone" :autosize="{ minRows: 2, maxRows: 4}" placeholder="Please input" />
        </el-form-item>
        <el-form-item label="电子邮箱">
          <el-input v-model="temp.email" :autosize="{ minRows: 2, maxRows: 4}"  placeholder="Please input" />
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

    <el-dialog title="关联经销商" :visible.sync="dialogDealerVisible" width="1000px" @close="oncloseDialog">
      <el-divider>已关联经销商</el-divider>
      <el-table :data="AssociatedDealerList" border fit >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
        <el-table-column label="经销商编码" prop="uuid" sortable="custom" align="center" width="120" />
        <el-table-column label="经销商名称" prop="name" sortable="custom" align="center" width="100" />
        <el-table-column label="经销商简称" prop="shortName" sortable="custom" align="center" width="120" />
        <el-table-column label="经销商类别" prop="categoryName" sortable="custom" align="center" width="120" />
        <el-table-column label="状态" prop="status" sortable="custom" align="center" width="100" />
        <el-table-column label="备注" prop="note" sortable="custom" align="center" width="150" />
        <el-table-column label="操作" align="center" width="144" class-name="small-padding fixed-width" prop="associated">
          <template slot-scope="{row}">
            <el-button size="mini" @click="removeAssociated(row)">
              取消关联
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-divider>所有经销商</el-divider>
      <el-table :data="MyAssociatedDealerList" border fit >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" prop="id" sortable="custom" align="center" width="50" :class-name="getSortClass('id')" />
        <el-table-column label="经销商编码" prop="uuid" sortable="custom" align="center" width="120" />
        <el-table-column label="经销商名称" prop="name" sortable="custom" align="center" width="100" />
        <el-table-column label="经销商简称" prop="shortName" sortable="custom" align="center" width="120" />
        <el-table-column label="经销商类别" prop="categoryName" sortable="custom" align="center" width="120" />
        <el-table-column label="状态" prop="status" sortable="custom" align="center" width="100" />
        <el-table-column label="备注" prop="note" sortable="custom" align="center" width="150" />
        <el-table-column label="操作" align="center" width="144" class-name="small-padding fixed-width" prop="associated">
          <template slot-scope="{row}">
            <el-button size="mini" :disabled="row.associated === false" @click="Associated(row)">
              关联
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>

</template>

<script>
import { fetchPv, fetchRoleList } from '@/api/article'
import { associatedDealer, removeAssociatedDealer, getAllAssociatedDealer, getAllCategory, getAllDealer, updateManageDealer, startOrStopManageDealer, delManageDealer, addManageDealer, searchAllManageDealer, getAllManageDealer } from '@/api/manage'
import waves from '@/directive/waves' // waves directive
import { getAssociatedAuthority, associatedAuthority } from '@/api/myrole'
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

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
        label: 'authorityName'
      },
      AssociatedDealerList: [],
      dialogDealerVisible: false,
      multipleSelection: [],
      RoleAuthority: [],
      AuthoritySelection: [],
      tableKey: 0,
      list: [],
      DealerList: [],
      SelectedAuthority: [],
      total: 0,
      roleList: [],
      userRole: [],
      listLoading: true,
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
        username: ''
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
        contactName: '',
        lastModifier: '',
        note: '',
        username: '',
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
      manageId: '',
      CategoryList: [],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      MyAssociatedDealerList: [],
      pvData: [],
      rules: {
        name: [{ required: true, message: '必填项不能为空', trigger: 'blur' }],
        username: [{ required: true, message: '必填项不能为空', trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getAllDealList()
  },
  methods: {
    oncloseDialog() {
      this.MyAssociatedDealerList = []
      this.AssociatedDealerList = []
      this.getAllDealList()
    },
    handleAssociatedDealerList(row) {
      getAllAssociatedDealer(row.id).then((response) => {
        this.manageId = row.id
        this.AssociatedDealerList = response.data
        this.dialogDealerVisible = true
        for (let i = 0; i < this.AssociatedDealerList.length; i++) {
          if (this.AssociatedDealerList[i].status === true) {
            this.AssociatedDealerList[i].status = '启用'
          }
          if (this.AssociatedDealerList[i].status === false) {
            this.AssociatedDealerList[i].status = '禁用'
          }
          if (this.AssociatedDealerList[i].managerId !== 0) {
            this.AssociatedDealerList[i].associated = false
          } else {
            this.AssociatedDealerList[i].associated = true
          }
          for (let j = 0; j < this.CategoryList.length; j++) {
            if (this.CategoryList[j].id === this.AssociatedDealerList[i].categoryId) {
              this.AssociatedDealerList[i]['categoryName'] = this.CategoryList[j].name
            }
          }
        }
        for (let j = 0; j < this.DealerList.length; j++) {
          let flag = true
          for (let i = 0; i < this.AssociatedDealerList.length; i++) {
            if (this.AssociatedDealerList[i].id === this.DealerList[j].id) {
              flag = false
            }
          }
          if (flag === true) {
            this.MyAssociatedDealerList.push(this.DealerList[j])
            console.log(this.DealerList[j])
          }
        }
      })
    },
    flush() {
      getAllAssociatedDealer(this.manageId).then((response) => {
        this.AssociatedDealerList = response.data
        this.dialogDealerVisible = true
        for (let i = 0; i < this.AssociatedDealerList.length; i++) {
          if (this.AssociatedDealerList[i].status === true) {
            this.AssociatedDealerList[i].status = '启用'
          }
          if (this.AssociatedDealerList[i].status === false) {
            this.AssociatedDealerList[i].status = '禁用'
          }
          if (this.AssociatedDealerList[i].managerId !== 0) {
            this.AssociatedDealerList[i].associated = false
          } else {
            this.AssociatedDealerList[i].associated = true
          }
          for (let j = 0; j < this.CategoryList.length; j++) {
            if (this.CategoryList[j].id === this.AssociatedDealerList[i].categoryId) {
              this.AssociatedDealerList[i]['categoryName'] = this.CategoryList[j].name
            }
          }
        }
        for (let j = 0; j < this.DealerList.length; j++) {
          let flag = true
          for (let i = 0; i < this.AssociatedDealerList.length; i++) {
            if (this.AssociatedDealerList[i].id === this.DealerList[j].id) {
              flag = false
            }
          }
          if (flag === true) {
            this.MyAssociatedDealerList.push(this.DealerList[j])
            console.log(this.DealerList[j])
          }
        }
      })
    },
    getAllgetAllCategory() {
      this.listLoading = true
      getAllCategory().then(response => {
        this.CategoryList = response.data
        for (let i = 0; i < this.total; i++) {
          if (response.data[i].status === true) {
            response.data[i].status = '启用'
          }
          if (response.data[i].status === false) {
            response.data[i].status = '禁用'
          }
          for (let i = 0; i < this.DealerList.length; i++) {
            if (this.DealerList[i].managerId !== 0) {
              this.DealerList[i].associated = false
            } else {
              this.DealerList[i].associated = true
            }
            for (let j = 0; j < this.CategoryList.length; j++) {
              if (this.CategoryList[j].id === this.DealerList[i].categoryId) {
                this.DealerList[i]['categoryName'] = this.CategoryList[j].name
              }
            }
          }
        }
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    getAllDealList() {
      this.listLoading = true
      getAllDealer().then(response => {
        this.DealerList = response.data
        for (let i = 0; i < this.total; i++) {
          if (response.data[i].status === true) {
            response.data[i].status = '启用'
          }
          if (response.data[i].status === false) {
            response.data[i].status = '禁用'
          }
        }

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
      this.$nextTick(() => {
        this.getAllgetAllCategory()
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
      startOrStopManageDealer(this.multipleSelection, flag).then(() => {
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
      getAllManageDealer().then(response => {
        this.list = response.data
        this.total = response.data.length
        for (let i = 0; i < this.total; i++) {
          if (response.data[i].status === true) {
            response.data[i].status = '启用'
          }
          if (response.data[i].status === false) {
            response.data[i].status = '禁用'
          }
        }

        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    handleFilter() {
      searchAllManageDealer(this.listQuery.name, this.listQuery.username, this.listQuery.status).then((response) => {
        this.list = response.data
        this.total = response.data.length
        for (let i = 0; i < this.total; i++) {
          if (response.data[i].status === true) {
            response.data[i].status = '启用'
          }
          if (response.data[i].status === false) {
            response.data[i].status = '禁用'
          }
        }
      })
    },
    removeAssociated(row) {
      removeAssociatedDealer(row.id, this.manageId).then(() => {
        this.oncloseDialog()
        this.flush()
      })
    },
    Associated(row) {
      associatedDealer(row.id, this.manageId).then(() => {
        this.oncloseDialog()
        this.flush()
      })
    },
    AllocationRole() {
      console.log(this.SelectedAuthority)
      const nodes = this.$refs.tree.getCheckedNodes()
      const arr = []
      nodes.forEach(e => {
        arr.push(e.id)
      })
      console.log(nodes)
      associatedAuthority([this.temp.id], arr).then(() => {
        this.dialogCheckRoleVisible = false
      })
      // allocationRole(this.checkRoleList, [this.temp.id]).then(() => {
      //   this.dialogCheckRoleVisible = false
      //   this.$notify({
      //     title: 'Success',
      //     message: '分配成功',
      //     type: 'success',
      //     duration: 2000
      //   })
      // })
    },
    showRole(row) {
      getAssociatedAuthority(row.id).then(response => {
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
          addManageDealer(this.temp).then(() => {
            this.list.push(this.temp)
            if (this.temp.status === false) {
              this.temp.status = '禁用'
            }
            if (this.temp.status === true) {
              this.temp.status = '启用'
            }
            this.getList()
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
          updateManageDealer(tempData).then(() => {
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
      delManageDealer([this.multipleSelection]).then(() => {
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
      delManageDealer([row.id]).then(() => {
      })
      this.$notify({
        title: 'Success',
        message: 'Delete Successfully',
        type: 'success',
        duration: 2000
      })
      this.list.splice(index, 1)
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
