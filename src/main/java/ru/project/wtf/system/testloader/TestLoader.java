package ru.project.wtf.system.testloader;

import ru.project.wtf.system.external.HasExternalSource;
import ru.project.wtf.system.loader.Loader;

/**
 * Интерфейс, с помощью которого производится загрузка теста в виде объекта из
 * файла и загрузка нового теста.
 * 
 * @see Test
 * @see Loader
 * @see HasExternalSource
 * 
 * @author Alimurad A. Ramazanov
 * @since 28.10.2017
 * @version 1.0.0
 *
 */
public interface TestLoader extends Loader<Test>, HasExternalSource {

}
