# TaskTracker
Проект представляет собой таск-менеджер, с возможностью создания проекта, статуса состояния задач в нем  и списка самих задач.
Реализован CRUD функционал, изменеие статуса, премещение задач между статусами по мере их выполнения.
REST API микросервис на стеке Spring+Java+SQL. 
Сервис организован в соответствии с подходом MVC.
Использовал JPA репозитории для работы с БД (Spring JPA), Hibernate для объектно-реляционного представления нашей БД.
Применял паттерн проектирования DTO для пердачи представления обьектов из базы данных на сторону клиента.
Для компоновки зависимостей использовал сборщик Maven.