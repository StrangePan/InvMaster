package org.jnbt;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R1.NBTBase;
import net.minecraft.server.v1_7_R1.NBTTagByte;
import net.minecraft.server.v1_7_R1.NBTTagByteArray;
import net.minecraft.server.v1_7_R1.NBTTagCompound;
import net.minecraft.server.v1_7_R1.NBTTagDouble;
import net.minecraft.server.v1_7_R1.NBTTagEnd;
import net.minecraft.server.v1_7_R1.NBTTagFloat;
import net.minecraft.server.v1_7_R1.NBTTagInt;
import net.minecraft.server.v1_7_R1.NBTTagIntArray;
import net.minecraft.server.v1_7_R1.NBTTagList;
import net.minecraft.server.v1_7_R1.NBTTagLong;
import net.minecraft.server.v1_7_R1.NBTTagShort;
import net.minecraft.server.v1_7_R1.NBTTagString;

/*
 * JNBT License
 * 
 * Copyright (c) 2010 Graham Edgecombe
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *       
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *       
 *     * Neither the name of the JNBT team nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */

/**
 * Represents a single NBT tag.
 * @author Graham Edgecombe
 *
 */
public abstract class Tag {
	
	/**
	 * Gets the value of this tag.
	 * @return The value of this tag.
	 */
	public abstract Object getValue();

	/**
	 * Gets the type of this tag.
	 * @return The type of this tag.
	 */
	public abstract TagType getTagType();
	
	/**
	 * Gets the data type of this tag.
	 * @return The data type of this tag.
	 */
	public abstract Type getDataType();
	
	/**
	 * Creates a new specialized tag based on the given data type
	 */
	@SuppressWarnings("unchecked")
	public static final Tag fromValue(Object o)
	{
		if (o.getClass() == byte[].class || o.getClass() == Byte[].class)
			return new ByteArrayTag((byte[]) o);
		else if (o.getClass() == byte.class || o.getClass() == Byte.class)
			return new ByteTag((byte) o);
		else if (o.getClass() == double.class || o.getClass() == Double.class)
			return new DoubleTag((double) o);
		else if (o.getClass() == float.class || o.getClass() == Float.class)
			return new FloatTag((float) o);
		else if (o.getClass() == int[].class || o.getClass() == Integer[].class)
			return new IntArrayTag((int[]) o);
		else if (o.getClass() == int.class || o.getClass() == Integer.class)
			return new IntTag((int) o);
		else if (o.getClass() == long.class || o.getClass() == Long.class)
			return new LongTag((long) o);
		else if (o.getClass() == short.class || o.getClass() == Short.class)
			return new ShortTag((short) o);
		else if (o.getClass() == String.class)
			return new StringTag((String) o);
		else if (o.getClass() == List.class || o.getClass() == ArrayList.class)
		{
			List<Tag> l = new ArrayList<Tag>();
			Type t = null;
			for (Object obj : (List<? extends Tag>) o)
			{
				Tag tag = Tag.fromValue(obj);
				l.add(tag);
				if (t == null)
					t = tag.getClass();
			}
			if (t == null)
				return new ListTag(ByteTag.class, new ArrayList<Tag>());
			else
				return new ListTag((Class<? extends Tag>) t, l);
		}
		else // Compound Tag Tyme!
		{
			return NBTUtils.objectToCompoundTag(o);
		}
			
	}
	
	/**
	 * Converts the tag into it's NBTBase counterpart.
	 * @return
	 */
	public abstract NBTBase toNBTTag();
	
	/**
	 * Converts an NBTTag into its jnbt counterpart.
	 * @param base
	 * @return Tag
	 */
	public static Tag fromNBTTag(NBTBase base)
	{
		if (base instanceof NBTTagByte)
			return ByteTag.fromNBTTag((NBTTagByte) base);
		else if (base instanceof NBTTagByteArray)
			return IntArrayTag.fromNBTTag((NBTTagIntArray) base);
		else if (base instanceof NBTTagShort)
			return ShortTag.fromNBTTag((NBTTagShort) base);
		else if (base instanceof NBTTagInt)
			return IntTag.fromNBTTag((NBTTagInt) base);
		else if (base instanceof NBTTagLong)
			return ByteArrayTag.fromNBTTag((NBTTagByteArray) base);
		else if (base instanceof NBTTagIntArray)
			return LongTag.fromNBTTag((NBTTagLong) base);
		else if (base instanceof NBTTagFloat)
			return FloatTag.fromNBTTag((NBTTagFloat) base);
		else if (base instanceof NBTTagDouble)
			return DoubleTag.fromNBTTag((NBTTagDouble) base);
		else if (base instanceof NBTTagString)
			return StringTag.fromNBTTag((NBTTagString) base);
		else if (base instanceof NBTTagList)
			return ListTag.fromNBTTag((NBTTagList) base);
		else if (base instanceof NBTTagCompound)
			return CompoundTag.fromNBTTag((NBTTagCompound) base);
		else if (base instanceof NBTTagEnd)
			return EndTag.fromNBTTag((NBTTagEnd) base);
		else
			return null;
	}
	
}
