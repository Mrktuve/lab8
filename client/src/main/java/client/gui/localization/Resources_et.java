package client.gui.localization;

import java.util.ListResourceBundle;

public class Resources_et extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return new Object[][]{

                {"auth.title", "Autoriseerimine"},
                {"auth.window.title", "Sisselogimine"},
                {"auth.language", "Keel"},

                {"auth.login.prompt", "Kasutajanimi"},
                {"auth.password.prompt", "Parool"},

                {"auth.login.button", "Logi sisse"},
                {"auth.register.button", "Registreeru"},

                {"auth.error.empty", "Täida väljad"},
                {"auth.error.network", "Võrgu viga"},
                {"auth.register.success", "Registreerimine õnnestus"},

                {"dialog.title", "Teade"},

                {"lang.ru", "Русский"},
                {"lang.et", "Eesti"},
                {"lang.lt", "Lietuvių"},
                {"lang.es", "Español (CR)"},

                {"main.title", "Kollektsioon"},

                {"canvas.empty", "Kollektsioon on tühi"},

                {"worker.dialog.edit.title", "redigeerimine  "},
                {"worker.dialog.edit.header", "töötaja redigeerimine"},
                {"worker.dialog.add.title", "Lisamise aken"},
                {"worker.dialog.add.header", "Töötaja lisamine"},
                {"worker.name", "Nimi"},
                {"worker.salary", "Palk"},
                {"worker.startDate", "Alguskuupäev"},
                {"worker.endDate", "Lõppkuupäev"},
                {"worker.status", "Töötaja staatus"},
                {"worker.person", "Isikuandmed"},
                {"person.passportID", "passi number/seeria"},
                {"person.eyeColor", "Silmade värv"},
                {"person.hairColor", "Juuste värv"},
                {"person.nationality", "Riik"},
                {"worker.coordinates", "X/Y"},
                {"worker.creationDate", "Loomise kuupäev"},
                {"worker.owner", "kasutajanimi"},

                {"button.ok", "nõustu"},
                {"button.cancel", "tühista"},
                {"error.empty.name", "Nimi on tühi"},
                {"error.invalid.salary", "palga viga"},
                {"error.invalid.start.date", "alguskuupäeva viga"},
                {"dialog.error", "Viga"},
                {"error.invalid.number", "vigane number"},

                {"main.filter", "Filter"},
                {"main.filter.name", "nime järgi"},
                {"main.sort", "Sorteerimine"},
                {"sort.name_asc", "nime järgi kasvavalt"},
                {"sort.name_desc", "nime järgi kahanevalt"},
                {"sort.salary_asc", "palga järgi kasvavalt"},
                {"sort.salary_desc", "palga järgi kahanevalt"},
                {"sort.id_asc", "id järgi kasvavalt"},

                {"main.info", "Teave"},
                {"status.loading", "Laadimine"},
                {"success.refresh", "edukas värskendamine"},
                {"error.load.collection", "kollektsiooni laadimise viga"},
                {"success.add", "edukalt lisatud"},
                {"success.add_if_max", "edukalt lisatud"},
                {"success.update", "edukalt uuendatud"},
                {"error.select.worker", "töötaja valimise viga"},
                {"dialog.confirm", "kinnitamise aken"},
                {"dialog.remove.confirm", "kinnitage kustutamine"},
                {"worker.id", "töötaja id"},
                {"success.remove", "edukalt eemaldatud"},
                {"dialog.clear.confirm", "tühjendamise hoiatus"},
                {"dialog.clear.warning", "Kinnitage tühjendamine"},
                {"success.clear", "edukas tühjendamine"},
                {"success.remove_lower", "edukalt eemaldatud väiksemad"},
                {"button.remove_by_status", "eemalda staatuse järgi"},
                {"worker.status", "töötaja staatus"},
                {"success.remove_by_status", "edukalt staatuse järgi eemaldatud"},
                {"dialog.logout", "logi välja"},
                {"dialog.logout.confirm", "kinnitage väljalogimine"},

                {"status.fired", "vallandatud"},
                {"status.recommended_for_promotion", "edutamiseks soovitatud"},
                {"status.regular", "töötab"},
                {"status.probation", "katseaeg"},

                {"info.total", "kokku"},
                {"info.owners", "kontod"},
                {"info.avg_salary", "keskmine palk"},
                {"status.ready", "valmis"},
                {"dialog.error", "viga"},
                {"status.error", "viga"},


                {"dialog.help", "abi aken"},
                {"button.help", "Käskude abi"},
                {"help.add", "add - lisa töötaja"},
                {"help.add_if_max", "add_if_max - lisa töötaja, kui see on teistest suurem"},
                {"help.update", "update - uuenda töötaja andmeid"},
                {"help.remove", "remove - eemalda töötaja"},
                {"help.clear", "clear - tühjenda kollektsioon"},
                {"help.refresh", "refresh - värskenda kollektsiooni"},
                {"help.info", "info - info kollektsiooni kohta"},
                {"help.removelower", "removelower - eemalda töötaja, kui see on väiksem sisestatud töötajast"},
                {"help.remove_by_status", "remove_by_status - eemalda töötaja staatuse järgi"},
                {"help.logout", "logout - logi kontolt välja"}
        };
    }
}