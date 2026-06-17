package client.gui.localization;

import java.util.ListResourceBundle;

public class Resources_es_CR extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return new Object[][]{

                {"auth.title", "Autorización"},
                {"auth.window.title", "Inicio de sesión"},
                {"auth.language", "Idioma"},

                {"auth.login.prompt", "Usuario"},
                {"auth.password.prompt", "Contraseña"},

                {"auth.login.button", "Entrar"},
                {"auth.register.button", "Registro"},

                {"auth.error.empty", "Complete los campos"},
                {"auth.error.network", "Error de red"},
                {"auth.register.success", "Registro exitoso"},

                {"dialog.title", "Mensaje"},

                {"lang.ru", "Русский"},
                {"lang.et", "Eesti"},
                {"lang.lt", "Lietuvių"},
                {"lang.es", "Español (CR)"},

                {"main.title", "Colección"},

                {"canvas.empty", "La colección está vacía"},

                {"worker.dialog.edit.title", "edición  "},
                {"worker.dialog.edit.header", "Edición de trabajador"},
                {"worker.dialog.add.title", "Ventana de agregar"},
                {"worker.dialog.add.header", "Agregar trabajador"},
                {"worker.name", "Nombre"},
                {"worker.salary", "Salario"},
                {"worker.startDate", "Fecha de inicio"},
                {"worker.endDate", "Fecha de fin"},
                {"worker.status", "Estado del trabajador"},
                {"worker.person", "Datos personales"},
                {"person.passportID", "número/serie de pasaporte"},
                {"person.eyeColor", "Color de ojos"},
                {"person.hairColor", "Color de cabello"},
                {"person.nationality", "País"},
                {"worker.coordinates", "X/Y"},
                {"worker.creationDate", "Fecha de creación"},
                {"worker.owner", "usuario"},

                {"button.ok", "aceptar"},
                {"button.cancel", "cancelar"},
                {"error.empty.name", "El nombre está vacío"},
                {"error.invalid.salary", "error en el salario"},
                {"error.invalid.start.date", "error en la fecha de inicio"},
                {"dialog.error", "Error"},
                {"error.invalid.number", "número inválido"},

                {"main.filter", "Filtro"},
                {"main.filter.name", "por nombre"},
                {"main.sort", "Ordenamiento"},
                {"sort.name_asc", "nombres ascendentes"},
                {"sort.name_desc", "nombres descendentes"},
                {"sort.salary_asc", "salario ascendente"},
                {"sort.salary_desc", "salario descendente"},
                {"sort.id_asc", "id ascendente"},

                {"main.info", "Información"},
                {"status.loading", "Cargando"},
                {"success.refresh", "recarga exitosa"},
                {"error.load.collection", "error al cargar la colección"},
                {"success.add", "agregado exitosamente"},
                {"success.add_if_max", "agregado exitosamente"},
                {"success.update", "actualizado exitosamente"},
                {"error.select.worker", "error al seleccionar trabajador"},
                {"dialog.confirm", "ventana de confirmación"},
                {"dialog.remove.confirm", "confirme la eliminación"},
                {"worker.id", "id del trabajador"},
                {"success.remove", "eliminado exitosamente"},
                {"dialog.clear.confirm", "advertencia de limpieza"},
                {"dialog.clear.warning", "Confirme la limpieza"},
                {"success.clear", "limpieza exitosa"},
                {"success.remove_lower", "menores eliminados exitosamente"},
                {"button.remove_by_status", "eliminar por estado"},
                {"worker.status", "estado del trabajador"},
                {"success.remove_by_status", "eliminado exitosamente por estado"},
                {"dialog.logout", "cerrar sesión"},
                {"dialog.logout.confirm", "confirme el cierre de sesión"},

                {"status.fired", "despedido"},
                {"status.recommended_for_promotion", "recomendado para promoción"},
                {"status.regular", "trabajando"},
                {"status.probation", "período de prueba"},

                {"info.total", "total"},
                {"info.owners", "cuentas"},
                {"info.avg_salary", "salario promedio"},
                {"status.ready", "listo"},
                {"dialog.error", "error"},
                {"status.error", "error"},


                {"dialog.help", "ventana de ayuda"},
                {"button.help", "Ayuda con comandos"},
                {"help.add", "add - agregar trabajador"},
                {"help.add_if_max", "add_if_max - agregar trabajador si es mayor que los demás"},
                {"help.update", "update - actualizar datos del trabajador"},
                {"help.remove", "remove - eliminar trabajador"},
                {"help.clear", "clear - limpiar colección"},
                {"help.refresh", "refresh - actualizar colección"},
                {"help.info", "info - información de la colección"},
                {"help.removelower", "removelower - eliminar trabajador si es menor que el trabajador ingresado"},
                {"help.remove_by_status", "remove_by_status - eliminar trabajador por estado"},
                {"help.logout", "logout - cerrar sesión"}
        };
    }
}