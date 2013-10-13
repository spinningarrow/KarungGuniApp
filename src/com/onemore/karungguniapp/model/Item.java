package com.onemore.karungguniapp.model;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/3/13
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */

public final class Item {

    private long itemId;
    private long endTime;
    private String picUrl;
//    private String smallPicUrl;

//    private String desc;
    private String category;
    private String addr_short;


    // favor "copy constructor/getInstance" over clone, clone is tricky and error prone
    // (better yet use immutable objects, but sort of overkill for this example)
    public static Item getInstance(final Item item) {
        Item copy = new Item();
//        copy.desc = item.desc;
        copy.endTime = item.endTime;
        copy.itemId = item.itemId;
       copy.addr_short = item.addr_short;
        copy.picUrl = item.picUrl;
//        copy.primaryCategoryName = item.primaryCategoryName;
//        copy.smallPicUrl = item.smallPicUrl;
        copy.category = item.category;
        return copy;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
//
//    public String getSmallPicUrl() {
//        return this.smallPicUrl;
//    }
//
//    public void setSmallPicUrl(String smallPicUrl) {
//        this.smallPicUrl = smallPicUrl;
//    }
//
//    public String getPic175Url() {
//        return this.pic175Url;
//    }
//
//    public void setPic175Url(String pic175Url) {
//        this.pic175Url = pic175Url;
//    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

//    public String getDesc() {
//        return this.desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public String getDealUrl() {
//        return this.dealUrl;
//    }
//
//    public void setDealUrl(String dealUrl) {
//        this.dealUrl = dealUrl;
//    }




//    public String getPrimaryCategoryName() {
//        return this.primaryCategoryName;
//    }

//    public void setPrimaryCategoryName(String primaryCategoryName) {
//        this.primaryCategoryName = primaryCategoryName;
//    }

    public String getLocation() {
        return this.addr_short;
    }

    public void setLocation(String addr_short) {
        this.addr_short = addr_short;
    }




    @Override
    public String toString() {
//        return "Item [convertedCurrentPrice=" + this.convertedCurrentPrice + ", dealUrl=" + this.dealUrl + ", desc="
//                + this.desc + ", endTime=" + this.endTime + ", hot=" + this.hot + ", itemId=" + this.itemId
//                + ", addr_short=" + this.addr_short + ", msrp=" + this.msrp + ", pic175Url=" + this.pic175Url + ", picUrl="
//                + this.picUrl + ", primaryCategoryName=" + this.primaryCategoryName + ", quantity=" + this.quantity
//                + ", quantitySold=" + this.quantitySold + ", savingsRate=" + this.savingsRate + ", smallPicUrl="
//                + this.smallPicUrl + ", category=" + this.category + "]";
                return "Item [ endTime=" + this.endTime + ", itemId=" + this.itemId
                +  ", category=" + this.category + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
//        result = prime * result + ((this.convertedCurrentPrice == null) ? 0 : this.convertedCurrentPrice.hashCode());
//        result = prime * result + ((this.dealUrl == null) ? 0 : this.dealUrl.hashCode());
//        result = prime * result + ((this.desc == null) ? 0 : this.desc.hashCode());
        // end time and hot not part of hashCode
        result = prime * result + (int) (this.itemId ^ (this.itemId >>> 32));
//        result = prime * result + ((this.addr_short == null) ? 0 : this.addr_short.hashCode());
//        result = prime * result + ((this.msrp == null) ? 0 : this.msrp.hashCode());
//        result = prime * result + ((this.pic175Url == null) ? 0 : this.pic175Url.hashCode());
//        result = prime * result + ((this.picUrl == null) ? 0 : this.picUrl.hashCode());
//        result = prime * result + ((this.primaryCategoryName == null) ? 0 : this.primaryCategoryName.hashCode());
        // quantity sold and quantity not part of hashCode
//        result = prime * result + ((this.savingsRate == null) ? 0 : this.savingsRate.hashCode());
//        result = prime * result + ((this.smallPicUrl == null) ? 0 : this.smallPicUrl.hashCode());
        result = prime * result + ((this.category == null) ? 0 : this.category.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Item other = (Item) obj;
//        if (this.convertedCurrentPrice == null) {
//            if (other.convertedCurrentPrice != null) {
//                return false;
//            }
//        } else if (!this.convertedCurrentPrice.equals(other.convertedCurrentPrice)) {
//            return false;
//        }
//        if (this.dealUrl == null) {
//            if (other.dealUrl != null) {
//                return false;
//            }
//        } else if (!this.dealUrl.equals(other.dealUrl)) {
//            return false;
//        }
//        if (this.desc == null) {
//            if (other.desc != null) {
//                return false;
//            }
//        } else if (!this.desc.equals(other.desc)) {
//            return false;
//        }
//        // end time and hot not part of equals
//        if (this.itemId != other.itemId) {
//            return false;
//        }
//        if (this.addr_short == null) {
//            if (other.addr_short != null) {
//                return false;
//            }
//        } else if (!this.addr_short.equals(other.addr_short)) {
//            return false;
//        }
//        if (this.msrp == null) {
//            if (other.msrp != null) {
//                return false;
//            }
//        } else if (!this.msrp.equals(other.msrp)) {
//            return false;
//        }
//        if (this.pic175Url == null) {
//            if (other.pic175Url != null) {
//                return false;
//            }
//        } else if (!this.pic175Url.equals(other.pic175Url)) {
//            return false;
//        }
//        if (this.picUrl == null) {
//            if (other.picUrl != null) {
//                return false;
//            }
//        } else if (!this.picUrl.equals(other.picUrl)) {
//            return false;
//        }
//        if (this.primaryCategoryName == null) {
//            if (other.primaryCategoryName != null) {
//                return false;
//            }
//        } else if (!this.primaryCategoryName.equals(other.primaryCategoryName)) {
//            return false;
//        }
//        // quanity sold and quanity not part of equals
//        if (this.savingsRate == null) {
//            if (other.savingsRate != null) {
//                return false;
//            }
//        } else if (!this.savingsRate.equals(other.savingsRate)) {
//            return false;
//        }
//        if (this.smallPicUrl == null) {
//            if (other.smallPicUrl != null) {
//                return false;
//            }
//        } else if (!this.smallPicUrl.equals(other.smallPicUrl)) {
//            return false;
//        }
        if (this.category == null) {
            if (other.category != null) {
                return false;
            }
        } else if (!this.category.equals(other.category)) {
            return false;
        }
        return true;
    }
}

