package client.gui.localization;

import java.util.ListResourceBundle;

public class Resources_ru extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return new Object[][]{

                {"auth.title", "Авторизация"},
                {"auth.window.title", "Вход"},
                {"auth.language", "Язык"},

                {"auth.login.prompt", "Логин"},
                {"auth.password.prompt", "Пароль"},

                {"auth.login.button", "Войти"},
                {"auth.register.button", "Регистрация"},

                {"auth.error.empty", "Заполните поля"},
                {"auth.error.network", "Ошибка сети"},
                {"error.network", "ошибка сети"},
                {"auth.register.success", "Регистрация успешна"},

                {"dialog.title", "Сообщение"},

                {"lang.ru", "Русский"},
                {"lang.et", "Eesti"},
                {"lang.lt", "Lietuvių"},
                {"lang.es", "Español (CR)"},

                {"main.title", "Коллекция"},
                {"canvas.empty", "Коллекция пуста"},

                {"worker.dialog.edit.title", "редактирование  "},
                {"worker.dialog.edit.header", "редактирование работника"},
                {"worker.dialog.add.title", "Окно добавления"},
                {"worker.dialog.add.header", "Добавление работника"},
                {"worker.name", "Имя"},
                {"worker.salary", "Зарплата"},
                {"worker.startDate", "Дата начала"},
                {"worker.endDate", "Дата конца"},
                {"worker.status", "Статус работника"},
                {"worker.person", "Личные данные"},
                {"person.passportID", "паспорт номер/серия"},
                {"person.eyeColor", "Цвет глаз"},
                {"person.hairColor", "Цвет волос"},
                {"person.nationality", "Страна"},
                {"worker.coordinates", "X/Y"},
                {"worker.creationDate", "Дата создания"},
                {"worker.owner", "логин"},

                {"button.ok", "принять"},
                {"button.cancel", "отменить"},
                {"error.empty.name", "Имя пусто"},
                {"error.invalid.salary", "ошибка в зп"},
                {"error.invalid.start.date", "ошибка в начальной дате"},
                {"dialog.error", "Ошибка"},
                {"error.invalid.number", "недействительный номер"},

                {"main.filter", "Фильтр"},
                {"main.filter.name", "по имени"},
                {"main.sort", "Сортировка"},
                {"sort.name_asc", "по возрастанию имен"},
                {"sort.name_desc", "по убыванию имен"},
                {"sort.salary_asc", "по возрастанию зп"},
                {"sort.salary_desc", "по убыванию зп"},
                {"sort.id_asc", "по возрастанию id"},

                {"main.info", "Информация"},
                {"status.loading", "Загружается"},
                {"success.refresh", "успешная перезагрузка"},
                {"error.load.collection", "ошибка загрузки коллекции"},
                {"success.add", "успешно добавлен"},
                {"success.add_if_max", "успешно добавлен"},
                {"success.update", "успешно обновлен"},
                {"error.select.worker", "ошибка выбора работника"},
                {"dialog.confirm", "окно подтверждения"},
                {"dialog.remove.confirm", "подтвердите удаление"},
                {"worker.id", "id работника"},
                {"success.remove", "успешно удален"},
                {"dialog.clear.confirm", "предупреждение об очистке"},
                {"dialog.clear.warning", "Подтвердите очистку"},
                {"success.clear", "успешная очистка"},
                {"success.remove_lower", "успешно удалены меньшие"},
                {"button.remove_by_status", "удалить по статусу"},
                {"worker.status", "статус работника"},
                {"success.remove_by_status", "успешно удален по статусу"},
                {"dialog.logout", "выйти"},
                {"dialog.logout.confirm", "подтвердите выход"},

                {"status.fired", "уволен"},
                {"status.recommended_for_promotion", "рекомендовано для продвижения"},
                {"status.regular", "работаешь"},
                {"status.probation", "испытательный срок"},

                {"info.total", "всего"},
                {"info.owners", "аккаунты"},
                {"info.avg_salary", "средняя зп"},
                {"status.ready", "готово"},
                {"dialog.error", "ошибка"},
                {"status.error", "ошибка"},


                {"dialog.help", "окно help"},
                {"button.help", "Помощь с командами"},
                {"help.add", "add - добавить работника"},
                {"help.add_if_max", "add_if_max - добавить работника если больше других"},
                {"help.update", "update - обновить данные работника"},
                {"help.remove", "remove - удалить работника"},
                {"help.clear", "clear - очистить коллекцию"},
                {"help.refresh", "refresh - обновить коллекцию"},
                {"help.info", "info - информация о коллекции"},
                {"help.removelower", "removelower - удалить работника, если меньше введеного работника"},
                {"help.remove_by_status", "remove_by_status - удалить работника по статусу"},
                {"help.logout", "logout - выйти из аккаунта"}

        };
    }
}