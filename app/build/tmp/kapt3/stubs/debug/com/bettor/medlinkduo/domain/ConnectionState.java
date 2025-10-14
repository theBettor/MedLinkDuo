package com.bettor.medlinkduo.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0006\u0002\u0003\u0004\u0005\u0006\u0007\u0082\u0001\u0006\b\t\n\u000b\f\r\u00a8\u0006\u000e"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState;", "", "Connecting", "Disconnected", "Discovering", "Idle", "Scanning", "Synced", "Lcom/bettor/medlinkduo/domain/ConnectionState$Connecting;", "Lcom/bettor/medlinkduo/domain/ConnectionState$Disconnected;", "Lcom/bettor/medlinkduo/domain/ConnectionState$Discovering;", "Lcom/bettor/medlinkduo/domain/ConnectionState$Idle;", "Lcom/bettor/medlinkduo/domain/ConnectionState$Scanning;", "Lcom/bettor/medlinkduo/domain/ConnectionState$Synced;", "app_debug"})
public abstract interface ConnectionState {
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState$Connecting;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "device", "Lcom/bettor/medlinkduo/domain/BleDevice;", "(Lcom/bettor/medlinkduo/domain/BleDevice;)V", "getDevice", "()Lcom/bettor/medlinkduo/domain/BleDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Connecting implements com.bettor.medlinkduo.domain.ConnectionState {
        @org.jetbrains.annotations.NotNull
        private final com.bettor.medlinkduo.domain.BleDevice device = null;
        
        public Connecting(@org.jetbrains.annotations.NotNull
        com.bettor.medlinkduo.domain.BleDevice device) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.BleDevice getDevice() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.BleDevice component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.ConnectionState.Connecting copy(@org.jetbrains.annotations.NotNull
        com.bettor.medlinkduo.domain.BleDevice device) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0010"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState$Disconnected;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "reason", "", "(Ljava/lang/String;)V", "getReason", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"})
    public static final class Disconnected implements com.bettor.medlinkduo.domain.ConnectionState {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String reason = null;
        
        public Disconnected(@org.jetbrains.annotations.NotNull
        java.lang.String reason) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getReason() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.ConnectionState.Disconnected copy(@org.jetbrains.annotations.NotNull
        java.lang.String reason) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState$Discovering;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "device", "Lcom/bettor/medlinkduo/domain/BleDevice;", "(Lcom/bettor/medlinkduo/domain/BleDevice;)V", "getDevice", "()Lcom/bettor/medlinkduo/domain/BleDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Discovering implements com.bettor.medlinkduo.domain.ConnectionState {
        @org.jetbrains.annotations.NotNull
        private final com.bettor.medlinkduo.domain.BleDevice device = null;
        
        public Discovering(@org.jetbrains.annotations.NotNull
        com.bettor.medlinkduo.domain.BleDevice device) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.BleDevice getDevice() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.BleDevice component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.ConnectionState.Discovering copy(@org.jetbrains.annotations.NotNull
        com.bettor.medlinkduo.domain.BleDevice device) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState$Idle;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Idle implements com.bettor.medlinkduo.domain.ConnectionState {
        @org.jetbrains.annotations.NotNull
        public static final com.bettor.medlinkduo.domain.ConnectionState.Idle INSTANCE = null;
        
        private Idle() {
            super();
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\n\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0013\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u00d6\u0003J\t\u0010\u0007\u001a\u00020\bH\u00d6\u0001J\t\u0010\t\u001a\u00020\nH\u00d6\u0001\u00a8\u0006\u000b"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState$Scanning;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Scanning implements com.bettor.medlinkduo.domain.ConnectionState {
        @org.jetbrains.annotations.NotNull
        public static final com.bettor.medlinkduo.domain.ConnectionState.Scanning INSTANCE = null;
        
        private Scanning() {
            super();
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/bettor/medlinkduo/domain/ConnectionState$Synced;", "Lcom/bettor/medlinkduo/domain/ConnectionState;", "device", "Lcom/bettor/medlinkduo/domain/BleDevice;", "(Lcom/bettor/medlinkduo/domain/BleDevice;)V", "getDevice", "()Lcom/bettor/medlinkduo/domain/BleDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Synced implements com.bettor.medlinkduo.domain.ConnectionState {
        @org.jetbrains.annotations.NotNull
        private final com.bettor.medlinkduo.domain.BleDevice device = null;
        
        public Synced(@org.jetbrains.annotations.NotNull
        com.bettor.medlinkduo.domain.BleDevice device) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.BleDevice getDevice() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.BleDevice component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.bettor.medlinkduo.domain.ConnectionState.Synced copy(@org.jetbrains.annotations.NotNull
        com.bettor.medlinkduo.domain.BleDevice device) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
}