package ru.project.wtf.system.pdf;

import java.io.File;

import ru.project.wtf.system.loader.Loader;
import ru.project.wtf.system.model.SystemObject;

/**
 * Интерфейс, предоставляющий службу в качестве выгрузчика файла теории в
 * формате pdf и нарезания на png.
 * 
 * @author Alimurad A. Ramazanov
 * @since 12.11.2017
 *
 */
public interface PdfLoader extends Loader<SystemObject<File>> {

}
