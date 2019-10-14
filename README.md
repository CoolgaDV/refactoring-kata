# Материалы для проведения refactoring workshop

Цель workshop-a - наглядно показать процесс рефакторинга на примере улучшения 
фрагмента кода. Участникам предстоит пройти следующие этапы:
- Поиск code smells;
- Проверка наличия unit тестов перед началом рефакторинга;
- Постепенная переработка кода с запуском unit тестов после каждой итерации;

## Программа

- Изучение 
  [фрагмента кода](
  src/main/java/cdv/workshop/refactoring/before/BankAccountHelper.java)
  и поиск code smells (индивидуально каждым из участников). 
- Групповое обсуждение найденных code smells и формирование общего списка.
  [Примерный список code smells]( 
  src/main/java/cdv/workshop/refactoring/before/BankAccountHelperWithComments.java)
  (см. комментарии начинающиеся с `FIXME`)
- Написание unit тестов (если времени не очень много, то можно воспользоваться 
  [готовым набором тестов]( 
  src/test/java/cdv/workshop/refactoring/before/BankAccountHelperTest.java)).
  Этот этап можно проходить как индивидуально каждым из участников, так и
  по очереди работая за одним компьютером.
- Как только unit тесты написаны и работают - начинаем атомарные изменения - 
  по одному за раз, при этом после каждого изменения прогоняем unit тесты.
  [Пример того, что должно получиться в результате](
  src/main/java/cdv/workshop/refactoring/after)
  и [соответствующие тесты](
  src/test/java/cdv/workshop/refactoring/after).
  Этот этап можно проходить как индивидуально каждым из участников, так и
  по очереди работая за одним компьютером.

## Список рекомендуемой литературы
- Refactoring: Improving the Design of Existing Code, Martin Fowler
- Clean Code: A Handbook of Agile Software Craftsmanship, Robert Martin
- Code Complete: A Practical Handbook of Software Construction, Steve MacConnell