import{H as y,r as l,A as C,j as e,B as b,K as I,z as S,N,S as p,O as m,M as x,I as h,J as P,W as k}from"./index-bea3f295.js";import{C as M}from"./Card-18a1e5ff.js";import{M as n}from"./CircularProgress-95998d00.js";import{L as U}from"./LoadingButton-45e3fa1b.js";import"./isMuiElement-3ad32d27.js";function A(){const a=y();return l.useMemo(()=>({back:()=>a(-1),forward:()=>a(1),reload:()=>window.location.reload(),push:t=>a(t),replace:t=>a(t,{replace:!0})}),[a])}const r=a=>a??"";function B(){const a=C(),u=A(),[t,g]=l.useState({}),[i,f]=l.useState(!1),[d,j]=l.useState(!1),w=()=>{P.post("/register",t).then(s=>{localStorage.setItem("token",s.data),setTimeout(()=>u.push("/"),500)})},o=(s,v)=>{const c={...t};c[s]=v,g(c)};return e.jsxs(b,{sx:{...I({color:S(a.palette.background.default,.9),imgUrl:"/assets/background/overlay_4.jpg"}),height:1},children:[e.jsx(N,{sx:{position:"fixed",top:{xs:16,md:24},left:{xs:16,md:24}}}),e.jsx(p,{alignItems:"center",justifyContent:"center",sx:{height:1},children:e.jsx(M,{sx:{p:5,width:1,maxWidth:420},children:e.jsxs(p,{spacing:3,children:[e.jsx(n,{name:"firstName",label:"First Name",value:r(t.firstName),onChange:s=>o("firstName",s.target.value)}),e.jsx(n,{name:"lastName",label:"Last Name",value:r(t.lastName),onChange:s=>o("lastName",s.target.value)}),e.jsx(n,{name:"email",label:"Email address",value:r(t.email),onChange:s=>o("email",s.target.value)}),e.jsx(n,{name:"password",label:"Password",type:i?"text":"password",value:r(t.password),onChange:s=>o("password",s.target.value),InputProps:{endAdornment:e.jsx(m,{position:"end",children:e.jsx(x,{onClick:()=>f(!i),edge:"end",children:e.jsx(h,{icon:i?"eva:eye-fill":"eva:eye-off-fill"})})})}}),e.jsx(n,{name:"password2",label:"Repeat Password",type:d?"text":"password",value:r(t.password2),onChange:s=>o("password2",s.target.value),InputProps:{endAdornment:e.jsx(m,{position:"end",children:e.jsx(x,{onClick:()=>j(!d),edge:"end",children:e.jsx(h,{icon:d?"eva:eye-fill":"eva:eye-off-fill"})})})}}),e.jsx(U,{fullWidth:!0,size:"large",type:"submit",variant:"contained",color:"inherit",onClick:w,children:"Sign up"})]})})})]})}function W(){return e.jsxs(e.Fragment,{children:[e.jsx(k,{children:e.jsx("title",{children:" Signup | My Moeny "})}),e.jsx(B,{})]})}export{W as default};
