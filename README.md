Для тестирования и отладки нужно создать бд в postgres - logistics 
с пользователем владеющим этой бд - logistics_admin c паролем - 1234

Через постман при запущенном сервере отправить POST запрос->
http://localhost:8080/signup
```json
body:
{
  "username":"courier",
  "password":"1111",
  "userStoreId":1,
  "location": "Moscow",
  "role":"COURIER"
}
```

Дальше можно логиниться
