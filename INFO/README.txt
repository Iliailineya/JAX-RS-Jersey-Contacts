Проект зроблений на базі JAX-RS-Jersey-Netty-Users.
Додане логування, написані тести (щоб не качати постман).


TEST REST API
---------------

Отримання всіх даних
GET
http://localhost:8082/api/v1.0/contacts

Отримання даних за id
GET
http://localhost:8082/api/v1.0/contacts/2

Створення даних
POST
http://localhost:8082/api/v1.0/contacts

Налаштування в Postman: Body, raw, JSON.

{
    "id": 5,
    "name": "John",
    "phone": "555 092 6565"
}

Оновлення даних за id
PUT
http://localhost:8082/api/v1.0/contacts/2

Налаштування в Postman: Body, raw, JSON.

{
    "phone": "555 092 6565"
}

Видалення даних за id
DELETE
http://localhost:8082/api/v1.0/contacts/3



