// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ImApi.proto

package com.codexiangli.im.common.api.proto;

public interface KeyLongValueOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.codexiangli.KeyLongValue)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>required string key = 1;</code>
   * @return Whether the key field is set.
   */
  boolean hasKey();
  /**
   * <code>required string key = 1;</code>
   * @return The key.
   */
  java.lang.String getKey();
  /**
   * <code>required string key = 1;</code>
   * @return The bytes for key.
   */
  com.google.protobuf.ByteString
      getKeyBytes();

  /**
   * <code>required uint64 value = 2;</code>
   * @return Whether the value field is set.
   */
  boolean hasValue();
  /**
   * <code>required uint64 value = 2;</code>
   * @return The value.
   */
  long getValue();
}
