Для тестирования и отладки нужно создать бд в postgres - logistics 
с пользователем владеющим этой бд - logistics_admin c паролем - 1234

Через постман при запущенном сервере запрос отправить ->
http://localhost:8080/
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
