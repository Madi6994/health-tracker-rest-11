<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Exercise Goals</h5>
          <div class="card-body">
            <h5 class="card-title">{{exerciseactivity.length}} exerciseactivity</h5>
            <a href="/exercisegoals" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class ="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Health Coaching</h5>
          <div class="card-body">
            <h5 class="card-title">{{healthactivity.length}} healthactivity</h5>
            <a href="/healthcoaching" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Heart Beat</h5>
          <div class="card-body">
            <h5 class="card-title">{{heartactivity.length}} heartactivity</h5>
            <a href="/heartbeats" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Step Counter</h5>
          <div class="card-body">
            <h5 class="card-title">{{coachingactivity.length}} coachingactivity</h5>
            <a href="/stepcounter" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Water Intake</h5>
          <div class="card-body">
            <h5 class="card-title">{{waterintakeactivity.length}} waterintakeactivity</h5>
            <a href="/trackingwaterintake" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        exerciseactivity: [],
        healthactivity: [],
        heartactivity:[],
        coachingactivity:[],
        waterintakeactivity:[]
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/exercisegoals")
            .then(res => this.exerciseactivity = res.data)
            .catch(() => alert("Error while fetching exercisegoals"));
        axios.get("/api/healthcoaching")
            .then(res => this.healthactivity = res.data)
            .catch(() => alert("Error while fetching healthcoaching"));
        axios.get("/api/heartbeats")
            .then(res => this.heartactivity = res.data)
            .catch(() => alert("Error while fetching heartbeats"));
        axios.get("/api/stepcounter")
            .then(res => this.coachingactivity = res.data)
            .catch(() => alert("Error while fetching stepcounter"));
        axios.get("/api/trackingwaterintake")
            .then(res => this.waterintakeactivity = res.data)
            .catch(() => alert("Error while fetching trackingwaterintake"));
      }
    });
</script>