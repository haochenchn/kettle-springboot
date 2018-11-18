package com.whhx.dataclean.service;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;

public interface KettleService {
    KettleDatabaseRepository DBRepositoryCon() throws KettleException;
    KettleFileRepository FRepositoryCon() throws KettleException;

}
