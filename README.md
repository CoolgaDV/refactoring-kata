# Материалы для проведения refactoring workshop

Цель workshop-a - наглядно показать процесс рефакторинга на примере улучшения 
фрагмента кода. Участникам предстоит пройти следующие этапы:
- Поиск code smells;
- Проверка наличия unit тестов перед началом рефакторинга;
- Постепенная переработка кода с запуском unit тестов после каждой итерации;

## Программа

- Изучение фрагмента кода и поиск code smells (индивидуально каждым из участников).
  Фрагмент кода для изучения: `cdv.workshop.refactoring.before.BankAccountHelper`
- Групповое обсуждение найденных code smells и формирование общего списка.
  Примерный список code smells (см. комментарии начинающиеся с `FIXME`): 
  `cdv.workshop.refactoring.before.BankAccountHelperWithComments`
- Написание unit тестов (если времени не очень много можно воспользоваться готовым
  набором тестов: `cdv.workshop.refactoring.before.BankAccountHelperTest`)
- Как только unit тесты написаны и работают - начинаем атомарные изменения - 
  по одному за раз, при этом после каждого изменения прогоняем unit тесты.
  Пример того, что должно получиться в результате:
  - `cdv.workshop.refactoring.after.MoneyTransferService`, 
  - `cdv.workshop.refactoring.after.CommissionCalculator`
  
  и соответствующие тесты
  
  - `cdv.workshop.refactoring.after.MoneyTransferServiceTest`
  - `cdv.workshop.refactoring.after.CommissionCalculatorTest`

## Список рекомендуемой литературы
- Refactoring: Improving the Design of Existing Code, Martin Fowler
- Clean Code: A Handbook of Agile Software Craftsmanship, Robert Martin
- Code Complete: A Practical Handbook of Software Construction, Steve MacConnell