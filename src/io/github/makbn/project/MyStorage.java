package io.github.makbn.project;

import org.cloudbus.cloudsim.File;
import org.cloudbus.cloudsim.Storage;

import java.util.List;

public class MyStorage implements Storage {

    private int maxReadTransferRate;
    private int maxWriteTransferRate;
    @Override
    public String getName() {
        return null;
    }

    @Override
    public double getCapacity() {
        return 0;
    }

    @Override
    public double getCurrentSize() {
        return 0;
    }

    @Override
    public double getMaxTransferRate() {
        return 0;
    }

    public int getMaxReadTransferRate() {
        return maxReadTransferRate;
    }

    public int getMaxWriteTransferRate() {
        return maxWriteTransferRate;
    }

    @Override
    public double getAvailableSpace() {
        return 0;
    }

    @Override
    public boolean setMaxTransferRate(int i) {
        return false;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int getNumStoredFile() {
        return 0;
    }

    @Override
    public boolean reserveSpace(int i) {
        return false;
    }

    @Override
    public double addReservedFile(File file) {
        return 0;
    }

    @Override
    public boolean hasPotentialAvailableSpace(int i) {
        return false;
    }

    @Override
    public File getFile(String s) {
        return null;
    }

    @Override
    public List<String> getFileNameList() {
        return null;
    }

    @Override
    public double addFile(File file) {
        return 0;
    }

    @Override
    public double addFile(List<File> list) {
        return 0;
    }

    @Override
    public File deleteFile(String s) {
        return null;
    }

    @Override
    public double deleteFile(String s, File file) {
        return 0;
    }

    @Override
    public double deleteFile(File file) {
        return 0;
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public boolean contains(File file) {
        return false;
    }

    @Override
    public boolean renameFile(File file, String s) {
        return false;
    }
}
