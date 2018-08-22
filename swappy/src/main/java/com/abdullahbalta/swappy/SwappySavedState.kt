package com.abdullahbalta.swappy

import android.os.Parcel
import android.os.Parcelable
import android.util.SparseArray
import android.view.View.BaseSavedState


internal class SwappySavedState : BaseSavedState {
    var childrenStates: SparseArray<Parcelable>? = null

    constructor(superState: Parcelable) : super(superState)

    private constructor(`in`: Parcel, classLoader: ClassLoader?) : super(`in`) {
        childrenStates = `in`.readSparseArray(classLoader) as SparseArray<Parcelable>?
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeSparseArray(childrenStates as SparseArray<Any>)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.ClassLoaderCreator<SwappySavedState> {
            override fun createFromParcel(source: Parcel, loader: ClassLoader?): SwappySavedState {
                return SwappySavedState(source, loader)
            }

            override fun createFromParcel(source: Parcel): SwappySavedState {
                return createFromParcel(source, null)
            }

            override fun newArray(size: Int): Array<SwappySavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}