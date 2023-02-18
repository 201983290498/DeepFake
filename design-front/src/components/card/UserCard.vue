<template>
  <div class="card">
    <form style="display: none;" ref="photoForm">
      <input type="file" name="photo"  v-on:change="readPic"/>
      <input type="text" name="userId" :value="user.userId"/>
    </form>
    <div slot="image">
      <img src="@/assets/img/background.jpg" alt="..." />
    </div>
    <div class="card-body">
      <div class="author">
        <div class="avatar border-white">
          <img :src="user.imageUrl" alt="..." style="height: 100%;width: 100%;" />
          <div class="mask hidden">点击更换头像</div>
        </div>
        <h4 class="title">{{user.username}}<br />
          <a href="#">
            <small>{{user.email}}</small>
          </a>
        </h4>
      </div>
      <p class="description text-center" >
        "I like the way you work it<br />No diggity <br />I wanna bag it up"
      </p>
      <hr/>
      <div class="text-center">
        <div class="row">
          <div v-for="(info, index) in details" :key="index" :class="getClasses(index)" id="details" >
            <h5>{{info.title}}<br/>
              <small>{{ info.subTitle }}</small>
            </h5>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import $ from 'jquery'
export default {
  name: 'UserCard',
  data () {
    return {
      details: [
        {
          title: '12',
          subTitle: 'Projects'
        },
        {
          title: '2GB',
          subTitle: 'Images'
        },
        {
          title: '24,6$',
          subTitle: 'Charge'
        }
      ],
      user: JSON.parse(this.$store.state.data)
    }
  },
  methods: {
    getClasses (index) {
      const remainder = index % 3
      if (remainder === 0) {
        return 'col-lg-3 col-lg-offset-1'
      } else if (remainder === 2) {
        return 'col-lg-4'
      } else {
        return 'col-lg-3'
      }
    },
    readPic: function (event) {
      const _this = this
      if (event.target.files.length === 0) {
        return
      }
      const file = event.target.files[0]
      if (!/^image\/\w*$/.test(file.type)) {
        event.target.value = ''
        _this.$message.warning('上传的并非图片,请重新选择')
        return
      }
      _this.$confirm('请选择是否确定更改头像', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.axios({
          url: window.server.COMMONS.userUrl + '/updatePhoto',
          method: 'post',
          data: new FormData(_this.$refs.photoForm)
        }).then(resp => {
          if (resp.data.result) {
            _this.user.imageUrl = resp.data.data
            _this.$store.commit('saveData', _this.user)
            _this.$emit('changeImage', resp.data.data)
          } else {
            _this.$message.warning('头像更新失败, 请稍后尝试.')
          }
        }).catch(() => {
          _this.$message.warning('头像更新失败, 请稍后尝试.')
        })
      })
    }
  },
  mounted () {
    $('.avatar').on('mouseover', () => {
      $('.mask').removeClass('hidden')
    })
    $('.avatar').on('mouseout', () => {
      $('.mask').addClass('hidden')
    })
    $('.avatar').on('click', () => {
      $('input[name=photo]').click()
    })
  }
}
</script>
<style scoped>
.card {
  text-align: center;
  font-family: "Muli", "Helvetica", Arial, sans-serif;
}
.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  position: relative;
  display: inline-block;
  margin: 0 auto 15px;
  overflow: hidden;
}
.avatar.border-white {
  border: 5px solid white;
}
.avatar.border-gray {
  border: 5px solid #ccc5b9;
}
.author {
  text-align: center;
  text-transform: none;
  margin-top: -78px;
  cursor: pointer;
}
.author .title{
  color: #403D39;
  font-weight: 600;
  margin-top: 25px;
  margin-bottom: 20px;
}
.author .title small{
  color: #ccc5b9;
  display: block;
}
.description {
  font-size: 16px;
  color: #66615b;
  font-family: "Muli", "Helvetica", Arial, sans-serif;
}
hr {
  border-color: #F1EAE0;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}
#details {
  margin-top: -10px;
}
#details small{
  display: block;
  margin-top: 5px;
  color: #9A9A9A;
  font-weight: 300;
  font-size: 1.5rem;
}
.mask {
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.7);
  position: absolute;
  top: 0;
  color: #F7F7F7;
  padding: 40% 0;
  text-align: center;
}
</style>
