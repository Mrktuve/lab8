package client.gui.localization;

import java.util.ListResourceBundle;

public class Resources_lt extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {

        return new Object[][]{

                {"auth.title", "Autorizacija"},
                {"auth.window.title", "Prisijungimas"},
                {"auth.language", "Kalba"},

                {"auth.login.prompt", "Prisijungimas"},
                {"auth.password.prompt", "Slaptažodis"},

                {"auth.login.button", "Prisijungti"},
                {"auth.register.button", "Registracija"},

                {"auth.error.empty", "Užpildykite laukus"},
                {"auth.error.network", "Tinklo klaida"},
                {"auth.register.success", "Registracija sėkminga"},

                {"dialog.title", "Pranešimas"},

                {"lang.ru", "Русский"},
                {"lang.et", "Eesti"},
                {"lang.lt", "Lietuvių"},
                {"lang.es", "Español (CR)"},

                {"main.title", "Kolekcija"},

                {"canvas.empty", "Kolekcija tuščia"},

                {"worker.dialog.edit.title", "redagavimas  "},
                {"worker.dialog.edit.header", "darbuotojo redagavimas"},
                {"worker.dialog.add.title", "Pridėjimo langas"},
                {"worker.dialog.add.header", "Darbuotojo pridėjimas"},
                {"worker.name", "Vardas"},
                {"worker.salary", "Atlyginimas"},
                {"worker.startDate", "Pradžios data"},
                {"worker.endDate", "Pabaigos data"},
                {"worker.status", "Darbuotojo statusas"},
                {"worker.person", "Asmeniniai duomenys"},
                {"person.passportID", "paso numeris/serija"},
                {"person.eyeColor", "Akių spalva"},
                {"person.hairColor", "Plaukų spalva"},
                {"person.nationality", "Šalis"},
                {"worker.coordinates", "X/Y"},
                {"worker.creationDate", "Sukūrimo data"},
                {"worker.owner", "prisijungimo vardas"},

                {"button.ok", "priimti"},
                {"button.cancel", "atšaukti"},
                {"error.empty.name", "Vardas tuščias"},
                {"error.invalid.salary", "atlyginimo klaida"},
                {"error.invalid.start.date", "pradžios datos klaida"},
                {"dialog.error", "Klaida"},
                {"error.invalid.number", "neteisingas numeris"},

                {"main.filter", "Filtras"},
                {"main.filter.name", "pagal vardą"},
                {"main.sort", "Rūšiavimas"},
                {"sort.name_asc", "pagal vardą didėjimo tvarka"},
                {"sort.name_desc", "pagal vardą mažėjimo tvarka"},
                {"sort.salary_asc", "pagal atlyginimą didėjimo tvarka"},
                {"sort.salary_desc", "pagal atlyginimą mažėjimo tvarka"},
                {"sort.id_asc", "pagal id didėjimo tvarka"},

                {"main.info", "Informacija"},
                {"status.loading", "Kraunama"},
                {"success.refresh", "sėkmingas atnaujinimas"},
                {"error.load.collection", "kolekcijos įkėlimo klaida"},
                {"success.add", "sėkmingai pridėta"},
                {"success.add_if_max", "sėkmingai pridėta"},
                {"success.update", "sėkmingai atnaujinta"},
                {"error.select.worker", "darbuotojo pasirinkimo klaida"},
                {"dialog.confirm", "patvirtinimo langas"},
                {"dialog.remove.confirm", "patvirtinkite ištrynimą"},
                {"worker.id", "darbuotojo id"},
                {"success.remove", "sėkmingai pašalinta"},
                {"dialog.clear.confirm", "išvalymo įspėjimas"},
                {"dialog.clear.warning", "Patvirtinkite išvalymą"},
                {"success.clear", "sėkmingas išvalymas"},
                {"success.remove_lower", "sėkmingai pašalinti mažesni"},
                {"button.remove_by_status", "pašalinti pagal statusą"},
                {"worker.status", "darbuotojo statusas"},
                {"success.remove_by_status", "sėkmingai pašalinta pagal statusą"},
                {"dialog.logout", "atsijungti"},
                {"dialog.logout.confirm", "patvirtinkite atsijungimą"},

                {"status.fired", "atleistas"},
                {"status.recommended_for_promotion", "rekomenduojamas paaukštinimui"},
                {"status.regular", "dirba"},
                {"status.probation", "bandomasis laikotarpis"},

                {"info.total", "iš viso"},
                {"info.owners", "paskyros"},
                {"info.avg_salary", "vidutinis atlyginimas"},
                {"status.ready", "paruošta"},
                {"dialog.error", "klaida"},
                {"status.error", "klaida"},


                {"dialog.help", "pagalbos langas"},
                {"button.help", "Komandų pagalba"},
                {"help.add", "add - pridėti darbuotoją"},
                {"help.add_if_max", "add_if_max - pridėti darbuotoją, jei didesnis už kitus"},
                {"help.update", "update - atnaujinti darbuotojo duomenis"},
                {"help.remove", "remove - pašalinti darbuotoją"},
                {"help.clear", "clear - išvalyti kolekciją"},
                {"help.refresh", "refresh - atnaujinti kolekciją"},
                {"help.info", "info - informacija apie kolekciją"},
                {"help.removelower", "removelower - pašalinti darbuotoją, jei mažesnis už įvestą darbuotoją"},
                {"help.remove_by_status", "remove_by_status - pašalinti darbuotoją pagal statusą"},
                {"help.logout", "logout - atsijungti nuo paskyros"}
        };
    }
}