from locust import HttpLocust, TaskSet, task

class UserBehavior(TaskSet):
    @task(1)
    def productpage(l):
	l.client.get("/a1")

    @task(1)
    def productpage(l):
	l.client.get("/a2")

class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000
