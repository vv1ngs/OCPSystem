<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.username" placeholder="用户名" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.realname" placeholder="真实名称" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.phone" placeholder="电话" style="width: 150px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.sex" placeholder="性别" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in sexList" :key="item" :label="item.label" :value="item.value" />
      </el-select>
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
      <el-table-column label="序号" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')" />
      <el-table-column label="用户名" prop="username" sortable="custom" align="center" width="150" />
      <el-table-column label="真实姓名" prop="realname" sortable="custom" align="center" width="150" />
      <el-table-column label="性别" prop="sex" sortable="custom" align="center" width="100" />
      <el-table-column label="手机号" prop="phone" sortable="custom" align="center" width="150" />
      <el-table-column label="邮箱" prop="email" sortable="custom" align="center" width="150" />
      <el-table-column label="状态" prop="status" sortable="custom" align="center" width="100" />
      <el-table-column label="操作" align="center" width="400" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑账号
          </el-button>
          <el-button size="mini" @click="handleAllocationRole(row)">
            编辑权限
          </el-button>
          <el-button size="mini" @click="showCategory(row)">
            查看权限
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
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

    <el-dialog :visible.sync="dialogCheckRoleVisible" title="分配权限">
      <el-form :model="SelectedAuthority" label-width="80px" label-position="left">
        <el-form-item label="Menus">
          <el-tree
            ref="tree"
            :data="categoryList"
            :props="AuthorityLabel"
            show-checkbox
            node-key="path"
            class="permission-tree"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCheckRoleVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="AllocationCategory">
          确认
        </el-button>
      </div>
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
  </div>
</template>

<script>
import { fetchPv, fetchRoleList } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { getChildCategory, assignCategory, getAllCategory, delDealer, startOrStopDealer, searchChildDealer, updateDealer, addChildDealer, getAllChildDealer } from '@/api/childManage'
import { getAllAuthority } from '@/api/myrole'
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
        label: 'name'
      },
      dialogCheckRoleVisible1: false,
      dialogCheckRoleVisible: false,
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
      downloadLoading: false
    }
  },
  created() {
    this.getList()
    this.getCategory()
  },
  methods: {
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
      console.log(this.SelectedAuthority)
      const nodes = this.$refs.tree.getCheckedNodes()
      const arr = []
      nodes.forEach(e => {
        arr.push(e.id)
      })
      console.log(nodes)
      assignCategory([this.temp.id], arr).then(() => {
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
            this.getList()
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
      delDealer([row.id]).then(() => {
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
